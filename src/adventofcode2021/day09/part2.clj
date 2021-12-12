(ns adventofcode2021.day09.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day09.part1 :as part1]))

(defn lower-point
  [pos->height pos]
  (apply min-key #(get pos->height % ##Inf)
         pos
         (part1/neighbor-positions pos)))

(def find-basin
  (memoize
   (fn [pos->height pos]
     (let [next-pos (lower-point pos->height pos)]
       (if (= pos next-pos)
         pos
         (find-basin pos->height next-pos))))))

(defn all-basins
  [pos->height]
  (let [all-positions (keys pos->height)
        non-nine-positions (filter #(not= 9 (get pos->height %)) all-positions)
        basin+position (map #(hash-map (find-basin pos->height %) #{(vec %)}) non-nine-positions)
        low-point->positions (apply merge-with clojure.set/union basin+position)
        basins (vals low-point->positions)]
    (apply * (take 3 (sort > (map count basins))))))

(defn solve
  [mat]
  (all-basins (part1/mat->map mat)))

(def solution
  {:parse part1/parse
   :solve solve})
