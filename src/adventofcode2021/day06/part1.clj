(ns adventofcode2021.day06.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-int [text] (Integer/parseInt text))
(defn parse
  [text]
  (->> (clojure.string/split text #",")
       (map parse-int)))

(defn next-day
  [timer-populations]
  (->> timer-populations
       (map (fn [[timer count]]
              (if (= timer 0)
                {6 count, 8 count}
                {(dec timer) count})))
       (apply merge-with +)))

(defn lanternfish-population-after-days
  [days timer-populations]
  (->> timer-populations
       (frequencies)
       (iterate next-day)
       (#(nth % days))
       (vals)
       (apply +)))

(defn solve
  [timer-populations]
  (lanternfish-population-after-days 80 timer-populations))

(def solution
  {:parse parse
   :solve solve})