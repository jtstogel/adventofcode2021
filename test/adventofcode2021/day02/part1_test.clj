(ns adventofcode2021.day02.part2-test
  (:require [clojure.test :refer [deftest is testing]]
            [adventofcode2021.day02.part1]))

(deftest part1
  (testing "parse" [(is (= (adventofcode2021.day02.part1/parse) "day02part1"))])
  (testing "solve" [(is (= (adventofcode2021.day02.part1/solve) "day02part1"))]))
