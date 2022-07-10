(ns adventofcode2021.day24.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day24.part1 :as part1]))

(defn smallest-next-digit
  [{:keys [z model]}
   {[a b c] :iteration-params :keys [zeroable-z?]}]
  (part1/first-non-nil
   (for [digit (range 1 10)]
     (let [next-z (part1/update-fn z a b c digit)]
       (when (zeroable-z? next-z)
         {:z next-z
          :model (+ digit (* 10 model))})))))

(defn solve
  [iteration-params-coll]
  (:model
   (reduce smallest-next-digit 
           {:z 0 :model 0} 
           (part1/annotate-zeroable-z-values iteration-params-coll))))

(def solution
  {:parse part1/parse
   :solve solve})
