(ns paths.handlers
  (:require [clojure.data.json :as json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [org.httpkit.server :refer [send! with-channel on-close on-receive]]
            [compojure.core :refer [defroutes routes GET]]
            [compojure.route :refer [resources]]
            [cognitect.transit :as t])

  (:import (java.io ByteArrayInputStream)))

(defonce orientations (atom {"p1" :right}))

(defn update-orientation! [player orientation]
  (swap! orientations (fn [o]
                        (update o player (fn [_] orientation)))))

(defonce channels (atom #{}))

(defn broadcast [msg]
  (doseq [channel @channels]
    (send! channel msg)))

(defn t-read [x]
  (let [bytes (.getBytes x)
        in (ByteArrayInputStream. bytes)
        rdr (t/reader in :json)]
    (t/read rdr)))

(defn connect! [channel]
  (swap! channels conj channel))

(defn disconnect! [channel status]
  (swap! channels #(remove #{channel} %)))

(defn receive-msg [msg]
  (update-orientation! "p1" (keyword (:orientation (t-read msg)))))

(defn ws-handler [request]
  (with-channel request channel
                (connect! channel)
                (on-close channel (partial disconnect! channel))
                (on-receive channel receive-msg)))

;(defn go [req]
;  (future
;    (run (board 50)
;         {"p1" {:x 0 :y 0}}
;         1000 [])
;    (response nil)))

;(defn post-player [req]
;  (swap! orientations-atom (fn [orientations]
;                             (update orientations (get-in req [:route-params :player]) (fn [_]
;                                                                                         (keyword (get-in req [:body :orientation]))
;                                                                                         ))))
;(println "post-player-start" (get-in req [:body]) "post-player-end")
;(response nil))

(defroutes all-routes
           ;(GET "/orientations" [] (fn [req] (json/write-str @orientations-atom)))
           ;(POST "/players/:player" [] (wrap-json-body post-player {:keywords? true}))
           (GET "/ws" [] ws-handler)
           ;(GET "/go" [] go)
           (resources "/"))

(def app
  (routes (-> all-routes
              (wrap-defaults api-defaults))))