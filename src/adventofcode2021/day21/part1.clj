(ns adventofcode2021.day21.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse
  [text]
  (let [[_ p1 _ p2] (re-seq #"\d+" text)]
    {:p1 {:pos (Integer/parseInt p1)
          :score 0}
     :p2 {:pos (Integer/parseInt p2)
          :score 0}
     :curr-player :p1}))

(defn move-player
  [pos distance]
  (inc (rem (dec (+ distance pos)) 10)))

(def other-player {:p1 :p2, :p2 :p1})

(defn make-move
  [{:keys [curr-player] :as game} distance]
  (let [next-pos (move-player (get-in game [curr-player :pos]) distance)
        next-score (+ (get-in game [curr-player :score]) next-pos)]
    (assoc game
           :curr-player (other-player curr-player)
           curr-player (assoc (game curr-player)
                              :pos next-pos
                              :score next-score))))

(defn play-game
  [game-init]
  (let [rolled-distances (->> (apply concat (repeat (range 1 101)))
                              (partition 3)
                              (map (partial apply +)))]
    (reductions make-move game-init rolled-distances)))

(defn solve
  [game-init]
  (let [game-states (take-while #(and (< (get-in % [:p1 :score]) 1000) (< (get-in % [:p2 :score]) 1000))
                                (play-game game-init))
        roll-count (* 3 (count game-states))
        final (last game-states)
        loser-score (min (get-in final [:p1 :score]) (get-in final [:p2 :score]))]
    (* roll-count loser-score)))

(def solution
  {:parse parse
   :solve solve})
