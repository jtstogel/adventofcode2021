(ns adventofcode2021.day05.part2
  (:require [adventofcode2021.day05.part1 :as part1]
            [clojure.string]
            [clojure.set]))

(defn solve
  [lines]
  (->> lines
       (mapcat part1/covered-points)
       (frequencies)
       (filter (fn [[_ count]] (< 1 count)))
       (count)))

(def solution
  {:parse part1/parse
   :solve solve})