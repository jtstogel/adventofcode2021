(ns adventofcode2021.core
  (:require [adventofcode2021.day01.part1]
            [adventofcode2021.day01.part2]
            [adventofcode2021.day02.part1]
            [adventofcode2021.day02.part2]
            [adventofcode2021.day03.part1]
            [adventofcode2021.day03.part2]
            [adventofcode2021.day04.part1]
            [adventofcode2021.day04.part2]
            [adventofcode2021.day05.part1]
            [adventofcode2021.day05.part2])
  (:gen-class))

(def solutions
  {"day01"
   {"part1" adventofcode2021.day01.part1/solution
    "part2" adventofcode2021.day01.part2/solution}
   "day02"
   {"part1" adventofcode2021.day02.part1/solution
    "part2" adventofcode2021.day02.part2/solution}
   "day03"
   {"part1" adventofcode2021.day03.part1/solution
    "part2" adventofcode2021.day03.part2/solution}
   "day04"
   {"part1" adventofcode2021.day04.part1/solution
    "part2" adventofcode2021.day04.part2/solution}
   "day05"
   {"part1" adventofcode2021.day05.part1/solution
    "part2" adventofcode2021.day05.part2/solution}})

(defn run-subdir
  [[day part input-file]]
  (let [{:keys [parse solve]} (get-in solutions [day part])]
    (println (solve (parse (slurp (str "src/adventofcode2021/" day "/" (or input-file "input.txt"))))))))

(defn -main [& args]
  ()
  (run-subdir args))
