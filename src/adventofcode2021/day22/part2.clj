(ns adventofcode2021.day22.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day22.part1 :as part1]))

(defn solve
  [toggles]
  (part1/count-toggled-on
   (reduce
    (fn [on-cuboids t]
      (part1/toggle on-cuboids (:power t) (:cuboid t)))
    []
    toggles)))

(def solution
  {:parse part1/parse
   :solve solve})
