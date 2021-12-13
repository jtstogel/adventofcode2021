(ns adventofcode2021.day13.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-coord
  [text]
  (vec (map #(Integer/parseInt %) (clojure.string/split text #","))))

(defn parse-instruction
  [text]
  (let [spec (subs text (count "fold along "))
        [axis-text n] (clojure.string/split spec #"=")]
    {:axis (keyword axis-text)
     :n (Integer/parseInt n)}))

(defn parse
  [text]
  (let [[coords-text instructions] (clojure.string/split text #"\n\n")]
    {:coordinates (set (map parse-coord (clojure.string/split-lines coords-text)))
     :instructions (vec (map parse-instruction (clojure.string/split-lines instructions)))}))

(defn fold-coordinate
  [{:keys [axis n]} [x y]]
  (case axis
    :x (when (not= n x)
         [(if (< x n) x (- (* 2 n) x)) y])
    :y (when (not= n y)
         [x (if (< y n) y (- (* 2 n) y))])))

(defn fold
  [coordinates instruction]
  (set (map (partial fold-coordinate instruction) coordinates)))

(defn do-folds
  [coordinates instructions]
  (reduce fold coordinates instructions))

(defn solve
  [{:keys [coordinates instructions]}]
  (count (do-folds coordinates (take 1 instructions))))

(def solution
  {:parse parse
   :solve solve})