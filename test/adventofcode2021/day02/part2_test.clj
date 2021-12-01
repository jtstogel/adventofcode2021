(ns adventofcode2021.day02.part2-test
  (:require [clojure.test :refer [deftest is testing]]
            [adventofcode2021.day02.part2]))

(deftest part2
  (testing "parse" [(is (= (adventofcode2021.day02.part2/parse) "day02part2"))])
  (testing "solve" [(is (= (adventofcode2021.day02.part2/solve) "day02part2"))]))
