(ns adventofcode2021.day02.part1-test
  (:require [clojure.test :refer [deftest is testing]]
            [adventofcode2021.day02.part1]))

(deftest part1
  (testing "parse" [(is (= (adventofcode2021.day02.part1/parse "forward 5\ndown 5\nforward 8\nup 3\ndown 8\nforward 2")
                           [[:forward 5] [:down 5] [:forward 8] [:up 3] [:down 8] [:forward 2]]))])
  (testing "solve" [(is (= (adventofcode2021.day02.part1/solve [[:forward 5] [:down 5] [:forward 8] [:up 3] [:down 8] [:forward 2]])
                           150))]))
