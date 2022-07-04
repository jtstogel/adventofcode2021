(ns adventofcode2021.day21.part2
  (:require [clojure.string]
            [clojure.set]
            [adventofcode2021.day21.part1 :as part1]))

(def distance->universe-count
  (frequencies
   (for [a (range 1 4), b (range 1 4), c (range 1 4)]
     (+ a b c))))

(defn make-nondeterministic-move
  [game-state->universe-count]
  (apply merge-with +
         (for [[game game-uni-cnt] game-state->universe-count
               [dist dist-uni-cnt] distance->universe-count]
           {(part1/make-move game dist) (* game-uni-cnt dist-uni-cnt)})))

(defn winner
  "Returns the winner or nil if the game is not yet over."
  [game]
  (first (filter #(<= 21 (get-in game [% :score])) [:p1 :p2])))

(defn solve
  [game-init]
  (loop [unfinised-game->universe-count {game-init 1}
         p1-total 0
         p2-total 0]
    (if (empty? unfinised-game->universe-count)
      (max p1-total p2-total)
      (let [game->universe-count (make-nondeterministic-move unfinised-game->universe-count)
            {p1-won :p1
             p2-won :p2
             unfinished nil} (group-by winner (keys game->universe-count))]
        (recur
         (select-keys game->universe-count unfinished)
         (apply + p1-total (vals (select-keys game->universe-count p1-won)))
         (apply + p2-total (vals (select-keys game->universe-count p2-won))))))))

(def solution
  {:parse part1/parse
   :solve solve})
