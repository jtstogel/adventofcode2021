(ns adventofcode2021.day10.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day10.part1 :as part1]))

(def chunk->score {\) 1, \] 2, \} 3, \> 4})

(defn score-closing-chunks
  [closing-chunks]
  (reduce (fn [acc chunk] (+ (chunk->score chunk) (* 5 acc)))
          0
          closing-chunks))

(defn score-unfinished
  [chunks]
  (let [{:keys [remaining-chunks closing-chunks]} (part1/process-chunks chunks)]
    (when (empty? remaining-chunks)
      (score-closing-chunks closing-chunks))))

(defn median
  "Returns the median of a collection of comparable elements, or nil if coll is empty."
  [coll]
  (nth (sort coll) (quot (count coll) 2) nil))

(defn solve
  [chunks-coll]
  (->> chunks-coll
       (keep score-unfinished)
       (median)))

(def solution
  {:parse part1/parse
   :solve solve})