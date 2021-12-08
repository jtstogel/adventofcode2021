(ns adventofcode2021.day09.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-int [text] (Integer/parseInt text))
(defn parse ; parses comma-separated list of integers
  [text]
  (map parse-int (clojure.string/split text #",")))

(defn parse-line
  [text]
  text)
(defn parse
  [text] ; parses lines of things
  (->>  text
        (clojure.string/split-lines)
        (map parse-line)))


(defn solve
  [value]
  value)

(def solution
  {:parse parse
   :solve solve})