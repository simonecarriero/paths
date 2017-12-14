(ns paths.core-test
  (:require [clojure.test :refer :all])
  (:require [paths.core :refer [run]]))

(deftest run-test
  (is (= (run [[{:trails #{}} {:trails #{}} {:trails #{}}]
               [{:trails #{}} {:trails #{}} {:trails #{}}]
               [{:trails #{}} {:trails #{}} {:trails #{}}]]
              {:a {:position {:x 0 :y 0} :orientation :down-right}}
              2 [])

         [[[{:trails #{}} {:trails #{}} {:trails #{}}]
           [{:trails #{}} {:trails #{}} {:trails #{}}]
           [{:trails #{}} {:trails #{}} {:trails #{}}]]

          [[{:trails #{}} {:trails #{}} {:trails #{}}]
           [{:trails #{}} {:trails #{:a}} {:trails #{}}]
           [{:trails #{}} {:trails #{}} {:trails #{}}]]

          [[{:trails #{}} {:trails #{}} {:trails #{}}]
           [{:trails #{}} {:trails #{:a}} {:trails #{}}]
           [{:trails #{}} {:trails #{}} {:trails #{:a}}]]])))
