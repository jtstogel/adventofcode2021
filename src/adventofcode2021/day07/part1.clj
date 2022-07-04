(ns adventofcode2021.day07.part1
  (:require [clojure.string]
            [clojure.set]
            [clojure.math.numeric-tower :as math]))

(defn parse-int [text] (Integer/parseInt text))
(defn parse
  [text]
  (map parse-int (clojure.string/split text #",")))

(defn fuel-cost
  [horizontal-positions unified-position]
  (->> horizontal-positions
       (map (comp math/abs (partial - unified-position)))
       (apply +)))

(defn solve
  [horizontal-positions]
  (let [best-position (apply min-key (partial fuel-cost horizontal-positions) (range 1 2000))]
    (fuel-cost horizontal-positions best-position)))

(def solution
  {:parse parse
   :solve solve})