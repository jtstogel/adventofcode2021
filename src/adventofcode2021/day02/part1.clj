(ns adventofcode2021.day02.part1
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
  [{:keys [depth horizontal] :as loc} [direction n]]
  (case direction
    :forward (assoc loc :horizontal (+ horizontal n))
    :up (assoc loc :depth (- depth n))
    :down (assoc loc :depth (+ depth n))))

(defn solve
  [directions]
  (->> directions
       (reduce move {:depth 0 :horizontal 0})
       (#(* (:depth %) (:horizontal %)))))

(def solution
  {:parse parse
   :solve solve})

(solve [[:forward 5] [:down 5] [:forward 8] [:up 3] [:down 8] [:forward 2]])
