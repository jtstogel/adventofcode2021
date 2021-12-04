(ns adventofcode2021.day04.part2
  (:require [clojure.string]
            [adventofcode2021.day04.part1 :as part1]))

(defn tails
  [seq]
  (reductions (fn [s _] (rest s)) seq seq))

(defn partials
  [seq]
  (->> seq
       (reverse)
       (tails)
       (map reverse)
       (reverse)
       (rest)))

(defn winning-bingo-boards
  [calls boards]
  (let [winning? (fn [board] (not (nil? (part1/bingo-score calls board))))]
    (filter winning? boards)))

(defn first-bingo-score
  [all-calls board]
  (->> (partials all-calls)
       (map #(part1/bingo-score % board))
       (filter (comp not nil?))
       (first)))

(defn solve
  [[calls boards]]
  (->> (partials calls)
       (map #(winning-bingo-boards % boards))
       (apply concat)
       (distinct)
       (last)
       (first-bingo-score calls)))

(def solution
  {:parse part1/parse
   :solve solve})
