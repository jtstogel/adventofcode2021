(ns adventofcode2021.day12.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day12.part1 :as part1]))

(defn visitable? [visit-count name]
  (let [big? (not (part1/small-cave? name))
        small-caves (filter part1/small-cave? (keys visit-count))
        small-cave-visisted-twice? (some #(<= 2 %) (map visit-count small-caves))
        times-visited (get visit-count name 0)]
    (or big?
        (= times-visited 0)
        (and (= times-visited 1) (not small-cave-visisted-twice?)))))

(defn paths
  ([cave->adjacent]
   (paths cave->adjacent {} "start"))
  ([cave->adjacent visit-count current-cave]
   (if (= "end" current-cave)
     1
     (let [adjacent-caves (cave->adjacent current-cave)
           visit-count-incl-curr (update visit-count current-cave (fnil inc 0))
           next-cave-candidates (clojure.set/select (partial visitable? visit-count-incl-curr) adjacent-caves)]
       (->> next-cave-candidates
            (map (partial paths cave->adjacent visit-count-incl-curr))
            (apply +))))))

(defn solve
  [cave->adjacent]
  (paths cave->adjacent))

(def solution
  {:parse part1/parse
   :solve solve})