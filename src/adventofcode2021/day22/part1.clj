(ns adventofcode2021.day22.part1
  (:require [clojure.set]
            [clojure.math.combinatorics :as combo]
            [clojure.string :as str]))

(defn parse-axis-interval
  [text]
  (let [[start end] (str/split (subs text 2) #"\.\.")]
    [(Integer/parseInt start), (inc (Integer/parseInt end))]))

(defn parse-toggle
  [text]
  (let [[power & axes] (str/split text #"[\s,]")]
    {:power (case power "on" :on "off" :off)
     :cuboid (mapv parse-axis-interval axes)}))

(defn parse
  [text]
  (mapv parse-toggle (str/split-lines text)))

(defn rm-empty
  [segment-coll]
  (filterv (fn [[a b]] (< a b)) segment-coll))

(defn segment-diff-intersect
  "Returns a 3-vec of [x - y, x & y, y - x].
   Each difference/intersection is a collection of segments (exclusive ranges)."
  [[a b :as x] [c d :as y]]
  (mapv rm-empty
        (cond (< c a) (vec (reverse (segment-diff-intersect y x)))
              ; for the remaining, a <= c
              (<= a b c d) [[x], [], [y]]
              (<= a c d b) [[[a c], [d b]], [[c d]], []]
              (<= a c b d) [[[a c]], [[c b]], [[b d]]])))

(defn segment-contains
  "Returns whether the first segment fully contains the seconds."
  [[a b] [c d]]
  (<= a c d b))

(defn cuboid-contains?
  "Whether the first cuboid fully contains the second."
  [[x0 y0 z0] [x1 y1 z1]]
  (and (segment-contains x0 x1)
       (segment-contains y0 y1)
       (segment-contains z0 z1)))


(defn cuboid-intersect
  "Returns the intersection of two cuboids, or nil if none exists."
  [[x0 y0 z0] [x1 y1 z1]]
  (let [[_ x0&x1] (segment-diff-intersect x0 x1)
        [_ y0&y1] (segment-diff-intersect y0 y1)
        [_ z0&z1] (segment-diff-intersect z0 z1)]
    (first (map vec (combo/cartesian-product x0&x1 y0&y1 z0&z1)))))

(defn cuboids-intersect?
  "Whether the two cuboids have an intersection"
  [a b]
  (not (nil? (cuboid-intersect a b))))

(defn split-cuboid
  "Splits the first cuboid along the provided axis
   to accommodate for the second cuboid."
  [a b axis]
  (let [[a-b a&b] (segment-diff-intersect (a axis) (b axis))
        axis-segments (->> (concat a-b a&b)
                           (sort-by first))]
    (for [segment axis-segments]
      (assoc a axis segment))))

(defn cuboid-diff
  "Returns a collection of cuboids represeting a-b."
  ([a b]
   (cuboid-diff a b 2))  ; start splitting on the z-axis.
  ([a b axis]
   (let [a-split-cuboids (split-cuboid a b axis)]
     (apply
      concat
      (for [a-split-cuboid a-split-cuboids]
        (cond (cuboid-contains? b a-split-cuboid) []
              (cuboids-intersect? b a-split-cuboid) (cuboid-diff a-split-cuboid b (dec axis))
              :else [a-split-cuboid]))))))

(defn toggle-off
  [on-cuboids cuboid]
  (mapcat #(cuboid-diff % cuboid) on-cuboids))

(defn toggle-on
  [on-cuboids cuboid]
  (conj
   (toggle-off on-cuboids cuboid)
   cuboid))

(defn toggle
  [on-cuboids power cuboid]
  (if (= power :on)
    (toggle-on  on-cuboids cuboid)
    (toggle-off on-cuboids cuboid)))

(defn segment-size
  [[a b]]
  (- b a))

(defn cuboid-size
  [cuboid]
  (apply * (map segment-size cuboid)))

(defn intersect-cuboids
  [cuboids region]
  (keep #(cuboid-intersect % region) cuboids))

(defn count-toggled-on
  [on-cuboids]
  (apply + (map cuboid-size on-cuboids)))

(def initialization-region [[-50 51] [-50 51] [-50 51]])

(defn solve
  [toggles]
  (count-toggled-on
   (reduce
    (fn [on-cuboids t]
      (-> on-cuboids
          (toggle (:power t) (:cuboid t))
          (intersect-cuboids initialization-region)))
    []
    toggles)))

(def solution
  {:parse parse
   :solve solve})
