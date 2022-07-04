(ns adventofcode2021.day18.part2
  (:require [adventofcode2021.day18.part1 :as part1]
            [clojure.math.combinatorics :as combo]))

(defn solve
  [operands]
  (->> (combo/permuted-combinations operands 2)
       (map (comp part1/magnitude (partial apply part1/snalfish+)))
       (apply max)))

(def solution
  {:parse part1/parse
   :solve solve})