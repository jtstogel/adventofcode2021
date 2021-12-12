(ns adventofcode2021.day11.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day11.part1 :as part1]))

(defn in-sync?
  [energies]
  (= 1 (count (set (flatten energies)))))

(defn solve
  [mat]
  (->> mat
       (iterate part1/step)
       (take-while (comp not in-sync?))
       (count)))

(def solution
  {:parse part1/parse
   :solve solve})
