(ns paths.core
  (:require [paths.board :refer :all]
            [paths.handlers :as h]
            [clojure.walk :refer [prewalk]]
            [clojure.data.json :as json]
            [org.httpkit.server :refer [run-server]]))

(defn run [board positions steps acc]
  (h/broadcast (json/write-str board))
  (Thread/sleep 100)
  (let [next-positions (next-positions positions @h/orientations)
        next-board (next-board board next-positions)
        next-acc (conj acc board)]
    (if (pos? steps)
      (recur next-board next-positions (dec steps) next-acc)
      next-acc)))

(defn board [n]
  (->> (for [row (range n)
             col (range n)] {:trails #{}})
       (partition n)
       (prewalk #(if (seq? %) (vec %) %))))

(defn -main
  "paths"
  [& args]
  (future
    (println "Starting in 5 seconds...")
    (Thread/sleep 5000)
    (let [board-size 50
          initial-positions {"p1" {:x 0 :y 0}}
          steps 1000]
      (run (board board-size) initial-positions steps [])))
  (run-server #'h/app {:port 8081}))