(ns adventofcode2021.day04.part2
  (:require [clojure.string]))

(defn parse-int [x] (Integer/parseInt x))
(defn parse-ints [sep x] (map parse-int (filter not-empty (clojure.string/split x sep))))
(defn parse-board [board-text]
  (->> board-text
       (clojure.string/split-lines)
       (map (partial parse-ints #"\s+"))))
(defn parse-calls [calls-text] (parse-ints #"," calls-text))

(defn parse
  [text]
  (let [[calls-text & board-texts] (clojure.string/split text #"\n\n")
        calls (parse-calls calls-text)
        boards (map parse-board board-texts)]
    [calls boards]))

(defn tails
  [seq]
  (reductions (fn [s _] (rest s)) seq seq))

(defn partials
  [seq]
  (->> seq
       (reverse)
       (tails)
       (map reverse)
       (reverse)
       (rest)))

(defn transpose
  [seqs]
  (apply map vector seqs))

(defn candidate-number-combos
  "Returns a sequence of numbers from the bingo board that could constitute a win."
  [board]
  (let [rows board
        cols (transpose board)]
    (concat rows cols)))

(defn bingo-score
  [calls board]
  (let [called? (set calls)
        candidate-wins? (fn [candidate] (every? called? candidate))
        candidates (candidate-number-combos board)
        winning-board? (some candidate-wins? candidates)
        unmarked-numbers (filter (comp not called?) (flatten board))]
    (when winning-board?
      (* (apply + unmarked-numbers) (last calls)))))

(defn winning-bingo-boards
  [calls boards]
  (let [winning? (fn [board] (not (nil? (bingo-score calls board))))]
    (filter winning? boards)))

(defn first-bingo-score
  [all-calls board]
  (->> (partials all-calls)
       (map #(bingo-score % board))
       (filter (comp not nil?))
       (first)))

(defn solve
  [[calls boards]]
  (->> (partials calls)
       (map #(winning-bingo-boards % boards))
       (apply concat)
       (distinct)
       (last)
       (first-bingo-score calls)))

(def solution
  {:parse parse
   :solve solve})
