(ns adventofcode2021.day09.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-int [text] (Integer/parseInt text))
(defn parse-line
  [text]
  (vec (map parse-int (clojure.string/split text #""))))
(defn parse
  [text]
  (->>  text
        (clojure.string/split-lines)
        (map parse-line)
        (vec)))

(defn mat->map
  [mat]
  (apply merge (for [i (range (count mat))
                     j (range (count (mat i)))]
                 {[i j] (get-in mat [i j])})))

(defn neighbor-positions
  [[i j]]
  [[i (inc j)] [(inc i) j] [i (dec j)] [(dec i) j]])

(defn find-basin-positions
  [pos->height]
  (letfn [(basin?
           [pos]
           (let [height (pos->height pos)
                 neighbor-heights (map #(get pos->height % ##Inf) (neighbor-positions pos))]
             (< height (apply min neighbor-heights))))]
    (filter basin? (keys pos->height))))

(defn solve
  [mat]
  (let [pos->height (mat->map mat)]
    (apply + (map (comp inc pos->height) 
                  (find-basin-positions pos->height)))))

(def solution
  {:parse parse
   :solve solve})