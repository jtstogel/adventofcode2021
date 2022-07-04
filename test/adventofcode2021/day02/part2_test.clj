(ns adventofcode2021.day02.part2-test
  (:require [clojure.test :refer [deftest is testing]]
            [adventofcode2021.day02.part2]))

(deftest part2
  (testing "parse" [(is (= (adventofcode2021.day02.part2/parse "forward 2\ndown 5\nup 3")
                           [[:forward 2] [:down 5] [:up 3]]))])
  (testing "solve" [(is (= (adventofcode2021.day02.part2/solve [[:forward 5] [:down 5] [:forward 8] [:up 3] [:down 8] [:forward 2]])
                           900))]))
