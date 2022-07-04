(ns adventofcode2021.day15.part1
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo]
            [clojure.set]
            [astar.core :as astar]))

(defn parse-line
  [text]
  (->> (str/split text #"")
       (mapv #(Integer/parseInt %) )))
(defn parse
  [text]
  (->> text
       (str/split-lines)
       (mapv parse-line)))

(defn mat-shape [mat] [(count mat) (count (mat 0))])

(defn in-bounds?
  [[height width] [i j]]
  (and (< i height)
       (<= 0 i)
       (< j width)
       (<= 0 j)))

(defn neighbor-positions
  [shape [i j]]
  (filterv (partial in-bounds? shape)
           [[i (inc j)] [(inc i) j] [i (dec j)] [(dec i) j]]))

(defn graphify
  [mat]
  (let [[height width :as shape] (mat-shape mat)
        all-positions (map vec (combo/cartesian-product (range height) (range width)))]
    {:graph (apply hash-map (mapcat #(vector % (neighbor-positions shape %)) all-positions))
     :dist (fn [_ to] (get-in mat to))
     :h (fn [_] 0)}))

(defn lowest-risk-path
  [mat]
  (let [[height width] (mat-shape mat)
        {:keys [graph dist h]} (graphify mat)]
    (astar/route graph dist h [0 0] [(dec height) (dec width)])))

(defn path-total-risk
  [mat path]
  (->> path
       (map (partial get-in mat))
       (apply +)))

(defn solve
  [mat]
  (path-total-risk mat (lowest-risk-path mat)))

(def solution
  {:parse parse
   :solve solve})
