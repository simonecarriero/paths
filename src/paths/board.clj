(ns paths.board
  (:require [clojure.set :as set]))

(defn cell [board position]
  ((board (:y position)) (:x position)))

(defn map-board [f board]
  (vec (map-indexed (fn [y row]
                      (vec (map-indexed (fn [x cell]
                                          (f cell {:x x :y y})) row))) board)))

(defn neighbor
  ([position orientation]
   (case orientation
     :left       {:y (:y position)       :x (dec (:x position))}
     :right      {:y (:y position)       :x (inc (:x position))}
     :up         {:y (dec (:y position)) :x (:x position)}
     :down       {:y (inc (:y position)) :x (:x position)}
     :up-right   {:y (dec (:y position)) :x (inc (:x position))}
     :up-left    {:y (dec (:y position)) :x (dec (:x position))}
     :down-right {:y (inc (:y position)) :x (inc (:x position))}
     :down-left  {:y (inc (:y position)) :x (dec (:x position))}))

  ([pose]
   (neighbor (:position pose) (:orientation pose))))

(defn mapm [f m]
  (zipmap (keys m) (map f (vals m))))

(defn invert [m]
  (reduce (fn [m [k v]] (update m v #(set (conj % k)))) {} m))

(defn next-players [players]
  (mapm #(merge % {:position (neighbor %)}) players))

(defn next-board [board next-players]
  (let [players-by-position (invert (mapm :position next-players))]
    (map-board (fn [cell position]
                 (update cell :trails set/union (get players-by-position position))) board)))

