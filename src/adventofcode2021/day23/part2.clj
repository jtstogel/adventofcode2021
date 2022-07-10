(ns adventofcode2021.day23.part2
  (:require [clojure.string :as str]
            [adventofcode2021.day23.part1 :as part1]))

(defn extend-rooms
  [text]
  (let [lines (vec (str/split-lines text))]
    (str/join
     "\r\n"
     (concat
      (subvec lines 0 3)
      ["  #D#C#B#A#"
       "  #D#B#A#C#"]
      (subvec lines 3)))))

(defn parse
  [text]
  (binding [part1/*room-size* 4]
    (part1/parse (extend-rooms text))))

(defn solve
  [initial-state]
  (binding [part1/*room-size* 4]
    (part1/solve initial-state)))

(def solution
  {:parse parse
   :solve solve})
