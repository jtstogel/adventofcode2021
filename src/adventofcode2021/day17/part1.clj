(ns adventofcode2021.day17.part1
  (:require [clojure.string :as str]
            [clojure.set]
            [clojure.math.numeric-tower :as math]))

(def sample {:x-rng [20 30] :y-rng [-10 -5]})

(defn parse
  [text]
  (let [nums (re-seq #"-?\d+" (str/trim text))
        [x-low x-high y-low y-high] (map #(Integer/parseInt %) nums)]
    {:x-rng [x-low x-high]
     :y-rng [y-low y-high]}))

(defn tri
  [n]
  (quot (* n (inc n)) 2))

(defn inv-tri
  "If t is a triangle number, returns its index, else nil."
  [t]
  (let [[s r] (math/exact-integer-sqrt (+ (* 8 t) 1))]
    (when (and (zero? r) (= 1 (mod s 2)))
      (quot (- s 1) 2))))

(defn highest-y-coordinate-reached
  [initial-y-velocity]
  (tri initial-y-velocity))

(defn find-y-velocity
  "Returns the y-velocity that will land at the provided y value at step k,
   or nil if there is no suitable velocity"
  [k y]
  (let [numerator (+ (tri (dec k)) y)]
    (when (zero? (mod numerator k))
      (quot numerator k))))

(defn find-x-velocity
  "Returns the x-velocity that will land at the provided x value at step k,
   or nil if there is no suitable velocity"
  [k x]
  (let [xv-intersect-x-before-stalling (find-y-velocity k x)
        xv-to-stall-at-x (inv-tri x)]
    (cond
      (and xv-intersect-x-before-stalling (<= k xv-intersect-x-before-stalling)) xv-intersect-x-before-stalling
      (and xv-to-stall-at-x (<= xv-to-stall-at-x k)) xv-to-stall-at-x
      :else nil)))

(defn landing-yv-at-step
  "Returns a collection of y velocities that will land in the target at step k."
  [{[y1 y2] :y-rng} k]
  (keep (partial find-y-velocity k)
        (range y1 (inc y2))))

(landing-yv-at-step sample 3)

(defn landing-xv-at-step
  "Returns a collection of x velocities that will land in the target at step k."
  [{[x1 x2] :x-rng} k]
  (keep (partial find-x-velocity k)
        (range x1 (inc x2))))

(defn landing-velocities-at-step
  "Returns a collection of velocities that will land in the target at step k."
  [target k]
  (for [xv (landing-xv-at-step target k)
        yv (landing-yv-at-step target k)]
    [xv yv]))

(defn landing-velocities
  "Returns a collection of velocities that will land in the target at some step."
  [{[_ x1] :x-rng :as target}]
  (->> (range 1 (* 100 (inc x1)))  ; ¯\_(ツ)_/¯
       (mapcat (partial landing-velocities-at-step target))))

(defn solve
  [target]
  (->> (landing-velocities target)
       (map second)
       (apply max)
       (highest-y-coordinate-reached)))

(def solution
  {:parse parse
   :solve solve})
