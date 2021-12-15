(ns adventofcode2021.day11.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-int [text] (Integer/parseInt text))
(defn parse-line
  [text]
  (vec (map parse-int (clojure.string/split text #""))))
(defn parse
  [text]
  (->> text
       (clojure.string/split-lines)
       (map parse-line)
       (vec)))

(defn sliding-window [n coll] (partition n 1 coll))

(defn fixed-point
  [f initial]
  (->> initial
       (iterate f)
       (sliding-window 2)
       (drop-while (fn [[a b]] (not= a b)))
       (ffirst)))

(defn mat-get [mat pos] (get-in mat pos nil))

(defn neighbor-positions
  [[i j]]
  [[(inc i) j] [i (inc j)] [(inc i) (inc j)]
   [(dec i) j] [i (dec j)] [(dec i) (dec j)]
   [(inc i) (dec j)] [(dec i) (inc j)]])

(defn neighbor-elements
  [mat pos]
  (keep (partial mat-get mat)
        (neighbor-positions pos)))

(defn map-mat
  "Constructs a 2d matrix by calling f on each [element [i j]] in mat."
  [f mat]
  (vec (for [i (range (count mat))]
         (vec (for [j (range (count (mat i)))]
                (f (mat-get mat [i j]) [i j]))))))

(defn energy-too-high? [energy] (< 9 energy))

(defn flash
  [mat]
  (letfn [(next-el [el pos]
            (if (or (zero? el) (energy-too-high? el))
              0
              (+ el (count (filter energy-too-high? (neighbor-elements mat pos))))))]
    (map-mat next-el mat)))

(defn step
  [mat]
  (->> mat
       ; increment all energies by 1
       (map-mat (fn [e _] (inc e)))
       ; repeatedly flash until everything's settled
       (fixed-point flash)))

(defn solve
  [mat]
  (->> mat
       (iterate step)
       (take (inc 100))
       (map #(count (filter zero? (flatten %))))
       (apply +)))

(def solution
  {:parse parse
   :solve solve})
