(ns adventofcode2021.day08.part2
  (:require [clojure.string]
            [clojure.set]
            [clojure.math.combinatorics :as combo]
            [adventofcode2021.day08.part1 :as part1]))

(defn translation
  [[a b c d e f g]]
  {#{a c b e f g} 0
   #{c f} 1
   #{a c d e g} 2
   #{a c d f g} 3
   #{b c d f} 4
   #{a b d f g} 5
   #{a b d e f g} 6
   #{a c f} 7
   #{a b c d e f g} 8
   #{a b c d f g} 9})

(def possible-translations
  (map translation (combo/permutations [\a \b \c \d \e \f \g])))

(defn translation-consistent?
  [unique-displays translation]
  (= (set (keys translation)) (set unique-displays)))

(defn find-workable-translation
  [unique-displays]
  (->> possible-translations
       (filter (partial translation-consistent? unique-displays))
       (first)))

(defn solve-problem
  [{:keys [unique-displays answer-displays]}]
  (let [lookup (find-workable-translation unique-displays)]
    (->> answer-displays
         (map (comp str lookup))
         (clojure.string/join)
         (#(Integer/parseInt %)))))

(defn solve
  [problems]
  (apply + (map solve-problem problems)))

(def solution
  {:parse part1/parse
   :solve solve})
