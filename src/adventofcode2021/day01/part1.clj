(ns adventofcode2021.day01.part1
  (:require [clojure.string]))

(defn parse
  [text]
  (->> text
       (clojure.string/split-lines)
       (map #(Integer/parseInt %))))

(defn solve
  [nums]
  (->> (partition 2 1 nums)
       (filter (partial apply <))
       (count)))

(def solution
  {:parse parse
   :solve solve})
