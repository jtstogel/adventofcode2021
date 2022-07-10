(ns adventofcode2021.day24.part1
  (:require [clojure.string :as str]
            [clojure.set]))

;; The program is a repitition of the pattern 14 times:
;; inp w
;; mul x 0
;; add x z
;; mod x 26
;; div z a  -- variable, a \in {1, 26}
;; add x b  -- variable, -15 <= b <= 15
;; eql x w
;; eql x 0
;; mul y 0
;; add y 25
;; mul y x
;; add y 1
;; mul z y
;; mul y 0
;; add y w
;; add y c  -- variable, 6 <= c <= 14
;; mul y x
;; add z y

(defn parse-lit-operand
  [instruction-text]
  (->> (str/split instruction-text #" ")
       (drop 2)
       (first)))

(defn parse
  "Parse just the variable [a b c] parameters."
  [program-text]
  (->> (str/split-lines program-text)
       (map parse-lit-operand)
       (partition 18)
       (mapv #(vector (Integer/parseInt (nth % 4))
                      (Integer/parseInt (nth % 5))
                      (Integer/parseInt (nth % 15))))))

;; The above can be translated to the following function:
(defn update-fn
  [z a b c w]
  (if (= (+ (mod z 26) b) w)
    (quot z a)
    (+ w c (* 26 (quot z a)))))

(defn in-digital-range? [n] (< 0 n 10))

(defn exists-w?
  "Where there exist a w such that (= z-out (update-fn z-in a b c w))."
  [z-in z-out a b c]
  (let [fst-offset (+ (mod z-in 26) b)
        z-in-quot (quot z-in a)
        snd-offset (+ c (* 26 z-in-quot))]
    (if (in-digital-range? fst-offset)
      (or (= z-out z-in-quot)
          (and (in-digital-range? (- z-out snd-offset))
               (not= z-out (+ fst-offset snd-offset))))
      (in-digital-range? (- z-out snd-offset)))))

(defn inverse-update-fn
  "Returns the values of z that would result in z-out."
  [z-out a b c]
  (let [z-out-quot (quot z-out 26)
        z-out-quot*26 (* z-out-quot 26)
        z-out*26 (* z-out 26)
        possible-z (concat [z-out-quot]
                           (map #(+ z-out-quot*26 %) (range 26))
                           (map #(+ z-out*26 %) (range 26)))]
    (->> possible-z
         (filter #(exists-w? % z-out a b c))
         (set))))

(defn inverses
  [zs [a b c]]
  (->> zs
       (map #(inverse-update-fn % a b c))
       (apply clojure.set/union)))

(defn annotate-zeroable-z-values
  "A collection where each element is a set of z values.
   The i-th set of z values are exactly those that,
   after iteration i, can still be brought to 0 by
   subsequent iterations."
  [iteration-params-coll]
  (mapv (fn [iteration-params zeroable-z?] 
          {:iteration-params iteration-params 
           :zeroable-z? zeroable-z?}) 
        iteration-params-coll 
        (->> (reverse iteration-params-coll) 
             (reductions inverses #{0}) 
             (reverse) 
             (drop 1))))

(defn first-non-nil
  [coll]
  (first (filter (comp not nil?) coll)))

(defn largest-next-digit
  [{:keys [z model]}
   {[a b c] :iteration-params :keys [zeroable-z?]}]
  (first-non-nil
   (for [digit (reverse (range 1 10))]
     (let [next-z (update-fn z a b c digit)]
       (when (zeroable-z? next-z)
         {:z next-z
          :model (+ digit (* 10 model))})))))

(defn solve
  [iteration-params-coll]
  (:model 
   (reduce largest-next-digit 
           {:z 0 :model 0} 
           (annotate-zeroable-z-values iteration-params-coll))))

(def solution
  {:parse parse
   :solve solve})
