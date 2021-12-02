(ns adventofcode2021.day02.part2
  (:require [clojure.string]))

(defn parse
  "Outputs a collection of [direction num] vectors,
   where direction is one of :forward, :down, :up."
  [text]
  (->> text
       (clojure.string/split-lines)
       (map #(clojure.string/split % #"\s+"))
       (map (fn [[dir n]] [(keyword dir) (Integer/parseInt n)]))))

(defn move
  [{:keys [depth horizontal aim] :as loc} [direction n]]
  (case direction
    :forward (assoc loc
                    :horizontal (+ horizontal n)
                    :depth (+ depth (* aim n)))
    :up (assoc loc :aim (- aim n))
    :down (assoc loc :aim (+ aim n))))

(defn solve
  [directions]
  (->> directions
       (reduce move {:depth 0 :horizontal 0 :aim 0})
       (#(* (:depth %) (:horizontal %)))))

(def solution
  {:parse parse
   :solve solve})
