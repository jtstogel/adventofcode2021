(ns adventofcode2021.core
  (:require [adventofcode2021.day01.part1]
            [adventofcode2021.day01.part2]
            [adventofcode2021.day02.part1]
            [adventofcode2021.day02.part2]
            [adventofcode2021.day03.part1]
            [adventofcode2021.day03.part2])
  (:gen-class))

(defn parse-args [args]
  (println "Arguments are ignored: " args))

(defn parse-line [line]
  (str " :: " line))

(defn print-line [line]
  (->> line
       parse-line
       println))

(def solutions
  {"day01"
   {"part1" adventofcode2021.day01.part1/solution
    "part2" adventofcode2021.day01.part2/solution}
   "day02"
   {"part1" adventofcode2021.day02.part1/solution
    "part2" adventofcode2021.day02.part2/solution}
   "day03"
   {"part1" adventofcode2021.day03.part1/solution
    "part2" adventofcode2021.day03.part2/solution}})

(defn run-subdir
  [[day part]]
  (let [{:keys [parse solve]} (get-in solutions [day part])]
    (println (solve (parse (slurp (str "src/adventofcode2021/" day "/input.txt")))))))

(defn -main [& args]
  ()
  (run-subdir args))
