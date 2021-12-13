(ns adventofcode2021.day13.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day13.part1 :as part1]))

(defn fmt-coordinates
  [coordinates]
  (let [width (inc (apply max (map first coordinates)))
        height (inc (apply max (map second coordinates)))]
    (clojure.string/join
     "\n"
     (for [y (range height)]
       (clojure.string/join
        (for [x (range width)]
          (if (coordinates [x y]) "#" ".")))))))

(defn solve
  [{:keys [coordinates instructions]}]
  (fmt-coordinates (part1/do-folds coordinates instructions)))

(def solution
  {:parse part1/parse
   :solve solve})
