(ns adventofcode2021.day01.part2
  (:require [clojure.string]))

(defn parse
  [text]
  (->> text
       (clojure.string/split-lines)
       (map #(Integer/parseInt %))))

(defn solve
  [nums]
  (->> (partition 3 1 nums)
       (map (partial apply +))
       (partition 2 1)
       (filter (partial apply <))
       (count)))

(def solution
  {:parse parse
   :solve solve})
