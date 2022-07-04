(ns adventofcode2021.day01.part1-test
  (:require [clojure.test :refer [deftest is testing]]
            [adventofcode2021.day01.part1]))

(deftest part1
  (testing "parse"
    [(is (= (adventofcode2021.day01.part1/parse "199\n200\n208\n263\n")
            [199 200 208 263]))])
  (testing "solve"
    [(is (= (adventofcode2021.day01.part1/solve
             [199 200 208 210 200 207 240 269 260 263])
            7))]))
