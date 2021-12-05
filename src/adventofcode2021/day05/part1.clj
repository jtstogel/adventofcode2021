(ns adventofcode2021.day05.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-int [x] (Integer/parseInt x))
(defn parse-coord
  [text]
  (map parse-int
       (clojure.string/split text #",")))
(defn parse-line-segment
  [text]
  (map parse-coord
       (clojure.string/split text #" -> ")))
(defn parse
  [text]
  (map parse-line-segment
       (clojure.string/split-lines text)))

(defn point-slope
  [[[x1 y1] [x2 y2]]]
  (if (= x1 x2)
    {:intercept {:x x1 :y 0} :slope ##Inf}
    (let [slope (/ (- y1 y2) (- x1 x2))
          y-intercept (- y1 (* slope x1))]
      {:intercept {:y y-intercept :x 0}
       :slope slope})))
(defn slope [line] (:slope (point-slope line)))
(defn vertical? [line] (= (slope line) ##Inf))
(defn horizontal? [line] (= (slope line) 0))
(defn eval-psf
  [{{:keys [x y]} :intercept m :slope} x-value]
  [x-value (+ y (* m (- x-value x)))])

(defn int-point? [[x y]] (and (int? x) (int? y)))
(defn abs-range [a b] (range (min a b) (inc (max a b))))

(defn covered-points
  "Returns all integral points covered by the provided line."
  [[[x0 y0] [x1 y1] :as line]]
  (let [psf (point-slope line)]
    (cond (= x0 x1) (->> (abs-range y0 y1)
                         (map #(vector x0 %)))
          (= y0 y1) (->> (abs-range x0 x1)
                         (map #(vector % y0)))
          :else (->> (abs-range x0 x1)
                     (map (partial eval-psf psf))
                     (filter int-point?)
                     (vec)))))

(defn solve
  [lines]
  (->> lines
       (filter #(or (horizontal? %) (vertical? %)))
       (mapcat covered-points)
       (frequencies)
       (filter (fn [[_ count]] (< 1 count)))
       (count)))

(def solution
  {:parse parse
   :solve solve})