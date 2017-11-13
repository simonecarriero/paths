(ns paths.core
  (:require [paths.board :refer :all]))

(defn run [board players steps acc]
  (let [next-players (next-players players)
        next-board (next-board board next-players)
        next-acc (conj acc board)]
    (if (pos? steps)
      (recur next-board next-players (dec steps) next-acc)
      next-acc)))