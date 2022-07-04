(ns adventofcode2021.solutions
  (:require [adventofcode2021.day01.part1]
            [adventofcode2021.day01.part2]
            [adventofcode2021.day02.part1]
            [adventofcode2021.day02.part2]
            [adventofcode2021.day03.part1]
            [adventofcode2021.day03.part2]
            [adventofcode2021.day04.part1]
            [adventofcode2021.day04.part2]
            [adventofcode2021.day05.part1]
            [adventofcode2021.day05.part2]
            [adventofcode2021.day06.part1]
            [adventofcode2021.day06.part2]
            [adventofcode2021.day07.part1]
            [adventofcode2021.day07.part2]
            [adventofcode2021.day08.part1]
            [adventofcode2021.day08.part2]
            [adventofcode2021.day09.part1]
            [adventofcode2021.day09.part2]
            [adventofcode2021.day10.part1]
            [adventofcode2021.day10.part2]
            [adventofcode2021.day11.part1]
            [adventofcode2021.day11.part2]
            [adventofcode2021.day12.part1]
            [adventofcode2021.day12.part2]
            [adventofcode2021.day13.part1]
            [adventofcode2021.day13.part2]
            [adventofcode2021.day14.part1]
            [adventofcode2021.day14.part2]
            [adventofcode2021.day15.part1]
            [adventofcode2021.day15.part2]
            [adventofcode2021.day16.part1]
            [adventofcode2021.day16.part2]
            [adventofcode2021.day17.part1]
            [adventofcode2021.day17.part2]
            [adventofcode2021.day18.part1]
            [adventofcode2021.day18.part2]
            [adventofcode2021.day19.part1]
            [adventofcode2021.day19.part2]
            [adventofcode2021.day20.part1]
            [adventofcode2021.day20.part2]
            [adventofcode2021.day21.part1]
            [adventofcode2021.day21.part2]
            [adventofcode2021.day22.part1]
            [adventofcode2021.day22.part2]
            [adventofcode2021.day23.part1]
            [adventofcode2021.day23.part2]
            [adventofcode2021.day24.part1]
            [adventofcode2021.day24.part2]
            [adventofcode2021.day25.part1]
            [adventofcode2021.day25.part2]))

(def registry
  {"day01" {"part1" adventofcode2021.day01.part1/solution
            "part2" adventofcode2021.day01.part2/solution}
   "day02" {"part1" adventofcode2021.day02.part1/solution
            "part2" adventofcode2021.day02.part2/solution}
   "day03" {"part1" adventofcode2021.day03.part1/solution
            "part2" adventofcode2021.day03.part2/solution}
   "day04" {"part1" adventofcode2021.day04.part1/solution
            "part2" adventofcode2021.day04.part2/solution}
   "day05" {"part1" adventofcode2021.day05.part1/solution
            "part2" adventofcode2021.day05.part2/solution}
   "day06" {"part1" adventofcode2021.day06.part1/solution
            "part2" adventofcode2021.day06.part2/solution}
   "day07" {"part1" adventofcode2021.day07.part1/solution
            "part2" adventofcode2021.day07.part2/solution}
   "day08" {"part1" adventofcode2021.day08.part1/solution
            "part2" adventofcode2021.day08.part2/solution}
   "day09" {"part1" adventofcode2021.day09.part1/solution
            "part2" adventofcode2021.day09.part2/solution}
   "day10" {"part1" adventofcode2021.day10.part1/solution
            "part2" adventofcode2021.day10.part2/solution}
   "day11" {"part1" adventofcode2021.day11.part1/solution
            "part2" adventofcode2021.day11.part2/solution}
   "day12" {"part1" adventofcode2021.day12.part1/solution
            "part2" adventofcode2021.day12.part2/solution}
   "day13" {"part1" adventofcode2021.day13.part1/solution
            "part2" adventofcode2021.day13.part2/solution}
   "day14" {"part1" adventofcode2021.day14.part1/solution
            "part2" adventofcode2021.day14.part2/solution}
   "day15" {"part1" adventofcode2021.day15.part1/solution
            "part2" adventofcode2021.day15.part2/solution}
   "day16" {"part1" adventofcode2021.day16.part1/solution
            "part2" adventofcode2021.day16.part2/solution}
   "day17" {"part1" adventofcode2021.day17.part1/solution
            "part2" adventofcode2021.day17.part2/solution}
   "day18" {"part1" adventofcode2021.day18.part1/solution
            "part2" adventofcode2021.day18.part2/solution}
   "day19" {"part1" adventofcode2021.day19.part1/solution
            "part2" adventofcode2021.day19.part2/solution}
   "day20" {"part1" adventofcode2021.day20.part1/solution
            "part2" adventofcode2021.day20.part2/solution}
   "day21" {"part1" adventofcode2021.day21.part1/solution
            "part2" adventofcode2021.day21.part2/solution}
   "day22" {"part1" adventofcode2021.day22.part1/solution
            "part2" adventofcode2021.day22.part2/solution}
   "day23" {"part1" adventofcode2021.day23.part1/solution
            "part2" adventofcode2021.day23.part2/solution}
   "day24" {"part1" adventofcode2021.day24.part1/solution
            "part2" adventofcode2021.day24.part2/solution}
   "day25" {"part1" adventofcode2021.day25.part1/solution
            "part2" adventofcode2021.day25.part2/solution}})
