(ns paths.board-test
  (:require [clojure.test :refer :all]
            [paths.board :refer :all]))

(deftest cell-test
  (is (= (cell [[:a :b] [:c :d]] {:x 1 :y 1}) :d)))

(deftest map-board-test
  (is (= (map-board (fn [cell position] {:value cell :p position})
                    [[:a :b]
                     [:c :d]])
         [[{:value :a :p {:x 0 :y 0}} {:value :b :p {:x 1 :y 0}}]
          [{:value :c :p {:x 0 :y 1}} {:value :d :p {:x 1 :y 1}}]])))

(deftest neighbor-test
  (is (= (neighbor {:x 1 :y 1} :left) {:x 0 :y 1}))
  (is (= (neighbor {:x 1 :y 1} :right) {:x 2 :y 1}))
  (is (= (neighbor {:x 1 :y 1} :up) {:x 1 :y 0}))
  (is (= (neighbor {:x 1 :y 1} :down) {:x 1 :y 2}))
  (is (= (neighbor {:x 1 :y 1} :up-right) {:x 2 :y 0}))
  (is (= (neighbor {:x 1 :y 1} :up-left) {:x 0 :y 0}))
  (is (= (neighbor {:x 1 :y 1} :down-right) {:x 2 :y 2}))
  (is (= (neighbor {:x 1 :y 1} :down-left) {:x 0 :y 2})))

(deftest invert-test
  (is (= (invert {:a 1 :b 1 :c 2}) {1 #{:a :b} 2 #{:c}})))

(deftest next-positions-test
  (is (= (next-positions {:p1 {:x 1 :y 1}} {:p1 :right})
         {:p1 {:x 2 :y 1}})))

(deftest next-board-test
  (is (= (next-board
           [[{:trails #{:a}} {:trails #{}}]
            [{:trails #{:a}} {:trails #{:b}}]]
           {:a {:x 1 :y 0}
            :b {:x 1 :y 0}})
         [[{:trails #{:a}} {:trails #{:a :b}}]
          [{:trails #{:a}} {:trails #{:b}}]])))

