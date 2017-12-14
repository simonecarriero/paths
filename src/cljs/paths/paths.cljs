(ns paths.paths
  (:require
    [clojure.string :as str]
    [cognitect.transit :as t]
    [keybind.core :as key]))

(enable-console-print!)

(defonce ws-chan (atom nil))

(defn row-to-str [row]
  (str/join "" (map #(if (empty? (get % "trails")) "_" "x") row)))

(defn board-to-str [board]
  (str/join "<br>" (map row-to-str board)))

(def json-reader (t/reader :json))

(def json-writer (t/writer :json))

(defn receive-transit-msg! [update-fn]
  (fn [msg]
    (update-fn
      (->> msg .-data (t/read json-reader)))))

(defn render-board [board]
  (-> js/document
      (.getElementById "board")
      (.-innerHTML)
      (set! (board-to-str board))))

(defn send-transit-msg!
  [msg]
  (if @ws-chan
    (.send @ws-chan (t/write json-writer msg))
    (throw (js/Error. "Websocket is not available!"))))

(key/bind! "left" ::left #(send-transit-msg! {:orientation :left}))
(key/bind! "right" ::right #(send-transit-msg! {:orientation :right}))
(key/bind! "up" ::up #(send-transit-msg! {:orientation :up}))
(key/bind! "down" ::down #(send-transit-msg! {:orientation :down}))

(defn make-websocket! [url]
  (if-let [chan (js/WebSocket. url)]
    (do
      (set! (.-onmessage chan) (receive-transit-msg! render-board))
      (reset! ws-chan chan)
      (println "Websocket connection established with: " url))
    (throw (js/Error. "Websocket connection failed!"))))

(make-websocket! (str "ws://" (.-host js/location) "/ws"))