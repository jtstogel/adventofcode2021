(ns adventofcode2021.day01.part2-test
  (:require [clojure.test :refer [deftest is testing]]
            [adventofcode2021.day01.part2]))

(deftest part2
  (testing "solve"
    [(is (= (adventofcode2021.day01.part2/solve
             [199 200 208 210 200 207 240 269 260 263])
            5))]))
