(ns adventofcode2021.day06.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day06.part1 :as part1]))

(defn solve
  [timer-populations]
  (part1/lanternfish-population-after-days 256 timer-populations))

(def solution
  {:parse part1/parse
   :solve solve})