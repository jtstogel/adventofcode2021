(ns adventofcode2021.day07.part2
  (:require [clojure.string]
            [clojure.set]
            [clojure.math.numeric-tower :as math]
            [adventofcode2021.day07.part1 :as part1]))

(defn triangle-number
  [n]
  (quot (* (inc n) n) 2))

(defn distance
  [from to]
  (triangle-number (math/abs (- from to))))

(defn fuel-cost
  [horizontal-positions unified-position]
  (->> horizontal-positions
       (map (comp (partial distance unified-position)))
       (apply +)))

(defn solve
  [horizontal-positions]
  (let [best-position (apply min-key (partial fuel-cost horizontal-positions) (range 1 1500))]
    (fuel-cost horizontal-positions best-position)))

(def solution
  {:parse part1/parse
   :solve solve})
