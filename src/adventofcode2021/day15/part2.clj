(ns adventofcode2021.day15.part2
  (:require [adventofcode2021.day15.part1 :as part1]))

(defn incv
  "Increments every element of coll by n, rolling over to 1 when exceeding 9."
  [coll]
  (mapv #(inc (mod % 9)) coll))

(defn expand
  [times f v]
  (->> (iterate f v)
       (take times)
       (apply concat)
       (vec)))

(defn expand-mat
  [n mat]
  (->> mat
       (map (partial expand n incv))    ; expand horizontally n times
       (expand n (partial mapv incv)))) ; expand vertically n times

(defn solve
  [mat]
  (let [expanded (expand-mat 5 mat)]
    (part1/path-total-risk expanded (part1/lowest-risk-path expanded))))

(def solution
  {:parse part1/parse
   :solve solve})
