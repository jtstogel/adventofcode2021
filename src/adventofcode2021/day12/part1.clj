(ns adventofcode2021.day12.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-line
  [text]
  (clojure.string/split text #"-"))
(defn rm-start [caves] (clojure.set/difference caves #{"start"}))
(defn parse
  [text]
  (->> text
       (clojure.string/split-lines)
       (map parse-line)
       (map (fn [[a b]] {a (rm-start #{b}), b (rm-start #{a})}))
       (apply merge-with clojure.set/union)))

(defn small-cave? [name]
  (= name (clojure.string/lower-case name)))

(defn count-paths
  ([cave->adjacent]
   (count-paths cave->adjacent #{} "start"))
  ([cave->adjacent visited current-cave]
   (if (= "end" current-cave)
     1
     (let [adjacent-caves (cave->adjacent current-cave)
           visited-small-caves (clojure.set/select small-cave? visited)
           next-cave-candidates (clojure.set/difference adjacent-caves visited-small-caves)
           visited-inc-curr (clojure.set/union #{current-cave} visited)]
       (->> next-cave-candidates
            (map (partial count-paths cave->adjacent visited-inc-curr))
            (apply +))))))

(defn solve
  [cave->adjacent]
  (count-paths cave->adjacent))

(def solution
  {:parse parse
   :solve solve})