(ns adventofcode2021.day17.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day17.part1 :as part1]))

(defn solve
  [target]
  (->> (part1/landing-velocities target)
       (set)
       (count)))

(def solution
  {:parse part1/parse
   :solve solve})
