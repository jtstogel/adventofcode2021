(ns adventofcode2021.day19.part2
  (:require [clojure.string]
            [clojure.set]
            [clojure.math.numeric-tower :as math]
            [clojure.math.combinatorics :as combo]
            [adventofcode2021.day19.part1 :as part1]))

(defn manhatten-dist
  [a b]
  (->> (map (comp math/abs -) a b)
       (apply +)))

(defn max-scanner-dist
  [scanner-group]
  (apply max
         (for [[s0 s1] (combo/combinations (vals scanner-group) 2)]
           (manhatten-dist (:offset s0) (:offset s1)))))

(defn solve
  [scanners]
  (->> scanners
       (part1/find-overlapping-beacons)
       (part1/combine-scanner-groups)
       (max-scanner-dist)))

(def solution
  {:parse part1/parse
   :solve solve})
