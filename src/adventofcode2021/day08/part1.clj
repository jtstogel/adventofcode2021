(ns adventofcode2021.day08.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-displays
  [text]
  (->> text
       (clojure.string/trim)
       (#(clojure.string/split % #"\s+"))
       (map set)
       (vec)))

(defn parse-line
  [text]
  (let [[unique-displays-text answer-displays-text] (clojure.string/split text #" \| ")]
    {:unique-displays (parse-displays unique-displays-text)
     :answer-displays (parse-displays answer-displays-text)}))

(defn parse
  [text] ; parses lines of things
  (->>  text
        (clojure.string/split-lines)
        (map parse-line)))

(defn count-unique
  [{:keys [answer-displays]}]
  (->> answer-displays
       (map count)
       (filter #{2, 3, 4, 7})
       (count)))

(defn solve
  [problems]
  (apply + (map count-unique problems)))

(def solution
  {:parse parse
   :solve solve})