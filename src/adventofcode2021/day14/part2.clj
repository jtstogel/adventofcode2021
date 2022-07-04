(ns adventofcode2021.day14.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day14.part1 :as part1]))

(defn solve
  [{:keys [template rules]}]
  (part1/solve-n 40 template rules))

(def solution
  {:parse part1/parse
   :solve solve})