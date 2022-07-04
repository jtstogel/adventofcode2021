(ns adventofcode2021.day16.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day16.part1 :as part1]))

(defn bool->int [b] (if b 1 0))

(defn evaluate
  [{:keys [type-id subpackets int-literal]}]
  (let [operands (map evaluate subpackets)]
    (case type-id
      0 (apply + operands)
      1 (apply * operands)
      2 (apply min operands)
      3 (apply max operands)
      4 int-literal
      5 (bool->int (apply > operands))
      6 (bool->int (apply < operands))
      7 (bool->int (apply = operands)))))

(defn solve
  [hex]
  (evaluate (part1/parse-packet (part1/hex->bin hex))))

(def solution
  {:parse part1/parse
   :solve solve})