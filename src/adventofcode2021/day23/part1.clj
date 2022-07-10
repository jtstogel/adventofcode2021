(ns adventofcode2021.day23.part1
  (:require [clojure.string :as str]
            [clojure.set]
            [clojure.math.numeric-tower :as math]
            [astar.core :as astar]))


; We'll use the following coordinate system:
; ####################################
; # 0  1  2  3  4  5  6  7  8  9  10 #
; ####### 12 ## 14 ## 16 ## 18 #######
;      ## 22 ## 24 ## 26 ## 28 ##
;      ##########################
;
; This makes pathing a little easier:
; - To get into the hallway from a room, continually subtract 10.
; - To move left and right in the hallway, just inc or dec.
;
; Additionally, each amphipod will be identified by its
; destination room, so amphipod "A" will have id 2, "B" id 4, etc.
;
; State will be a map from each amphipod's position to the
; amphipod's identifier, so for the example below,
;
; #############
; #...A.......#
; ###D#.#C#D###
;   #C#A#B#B#
;   #########
;
; {3 2,  24 2,  ; A amphipods
;  26 4, 28 4,  ; B amphipods
;  22 6, 16 6,  ; C amphipods
;  12 8, 18 8}  ; D amphipods
;

(def ^:dynamic *room-size* 2)

(defn hallway? [pos] (<= 0 pos 10))
(defn room? [pos] (> pos 10))
(defn destiation-hallway-pos
  "Returns hallway position just outside the amphipod's destination room."
  [amphipod]
  amphipod)

(defn outside-room?
  [pos]
  (and (<= 2 pos 8)
       (even? pos)))

(defn construct-position
  "Constructs a position."
  [hallway-pos room-depth]
  (+ hallway-pos (* 10 room-depth)))

(defn hallway-pos+room-depth
  "Deconstructs a position."
  [pos]
  (if (= pos 10)
    [pos 0]
    [(mod pos 10) (quot pos 10)]))

#_(defn ltr
    [state pos]
    (case (state pos)
      2 "A", 4 "B", 6 "C", 8 "D"
      nil "."))

#_(defn visual-room-row
    [s d]
    (str/join "#" (map #(ltr s (construct-position % d))
                       [2 4 6 8])))

#_(defn visual-state
    [s]
    (str/join
     (concat
      ["#############\r\n"
       "#" (str/join (map (partial ltr s) (range 0 11))) "#\r\n"
       "###" (visual-room-row s 1) "###\r\n"]
      (apply concat
             (for [d (range 2 (inc *room-size*))]
               ["  #" (visual-room-row s d) "#  \r\n"]))
      ["  #########  \r\n"])))

(defn amphipod-starting-positions
  []
  (for [d (range 1 (inc *room-size*))
        h [2 4 6 8]]
    (construct-position h d)))

(defn destination-positions
  [amphipod]
  (map #(construct-position (destiation-hallway-pos amphipod) %)
       (range 1 (inc *room-size*))))

(defn distance
  "Returns the number of steps it takes to traverse from -> to."
  [from to]
  (let [[from-hallway-pos from-room-depth] (hallway-pos+room-depth from)
        [to-hallway-pos   to-room-depth]   (hallway-pos+room-depth to)]
    (if (= from-hallway-pos to-hallway-pos)
      (math/abs (- from-room-depth to-room-depth))
      (+ from-room-depth
         (math/abs (- from-hallway-pos to-hallway-pos))
         to-room-depth))))

(def energy-per-step
  {2 1, 4 10, 6 100, 8 1000})

(defn energy
  [amphipod from to]
  (* (distance from to)
     (energy-per-step amphipod)))

(defn path-range
  "Returns a collection of the numbers between start and end.
   Like (range start end), but uses (exclusive, inclusive]
   and behaves well when start > end."
  [start end]
  (if (= start end)
    nil
    (let [diff (- end start)
          dir (quot diff (math/abs diff))]
      (range (+ dir start) (+ dir end) dir))))

(defn path
  "Returns a collection of coordinates encountered to traverse from->to.
   from is excluded, while to is included."
  [from to]
  (let [[from-hallway-pos from-room-depth] (hallway-pos+room-depth from)
        [to-hallway-pos   to-room-depth]   (hallway-pos+room-depth to)]
    (cond
      (= from to)
      nil

      (= from-hallway-pos to-hallway-pos)
      (map #(construct-position from-hallway-pos %)
           (path-range from-room-depth to-room-depth))

      :else
      (concat (map #(construct-position from-hallway-pos %)
                   (path-range from-room-depth 0))
              (map #(construct-position % 0)
                   (path-range from-hallway-pos to-hallway-pos))
              (map #(construct-position to-hallway-pos %)
                   (path-range 0 to-room-depth))))))

(defn kv->map
  [kv-pairs]
  (apply hash-map (apply concat kv-pairs)))

(defn path-blockers
  [state path]
  (filter state path))

(defn unobstructed-subpath
  [state path]
  (take-while (comp not (partial contains? state)) path))

(defn stranger-positions-in-dest-room
  [amphipod state]
  (filter
   #(when-let [other (state %)]
      (not= amphipod other))
   (destination-positions amphipod)))

(defn in-destination-room?
  [pos state]
  (let [amphipod (state pos)
        [hallway-pos _] (hallway-pos+room-depth pos)]
    (= (destiation-hallway-pos amphipod)
       hallway-pos)))

(defn happily-in-destination-room?
  "Whether the amphipod is in the destination room
   and doesn't need to move ever again."
  [pos state]
  (let [amphipod (state pos)]
    (and (in-destination-room? pos state)
         (every?
          (fn [other-pos]
            (or (= pos other-pos) ; either it's me
                (and (> other-pos pos) ; or it's below me and filled with an amphipod
                     (= amphipod (state other-pos)))
                (< other-pos pos))) ; or it's above me, and I don't care 
          (destination-positions amphipod)))))

(defn available-positions
  "Returns a collection of positions that are unobstructed from pos."
  [pos state]
  (let [amphipod (state pos)

        unobstructed-path-to
        (fn [hallway-pos room-depth]
          (unobstructed-subpath state (path pos (construct-position hallway-pos room-depth))))

        paths-into-hallway
        (when (room? pos)  ; can only move into a hallway from a room
          (filter
           #(and (not (outside-room? %))
                 (hallway? %))
           (concat (unobstructed-path-to 0 0)
                   (unobstructed-path-to 10 0))))

        allowed-in-dest-room?  ; can only move into dest room if it contains only like amphipods
        (empty? (stranger-positions-in-dest-room amphipod state))

        available-dest-pos
        (when allowed-in-dest-room?
          (last  ; only ever go into the last slot available, since it'll have to go there eventually 
           (filter (set (destination-positions amphipod))
                   (unobstructed-path-to (destiation-hallway-pos amphipod) *room-size*))))]
    (when (not (happily-in-destination-room? pos state))
      (if (not (nil? available-dest-pos))
        [available-dest-pos] ; when we can directly go into a room, do it
        (distinct paths-into-hallway)))))

(defn hallway-deadlock?
  [state]
  (let [hallway-positions (filter hallway? (keys state))
        pos->hallway-blockers
        (kv->map (for [pos hallway-positions]
                   [pos (->> (destiation-hallway-pos (state pos))
                             (path pos)
                             (path-blockers state)
                             (filter hallway?)
                             (set))]))]
    (some identity
          (for [pos hallway-positions
                blocker-pos (pos->hallway-blockers pos)]
            (contains? (pos->hallway-blockers blocker-pos) pos)))))

(defn graph
  [state]
  (for [[pos amphipod] state
        next-pos (available-positions pos state)]
    (-> state
        (dissoc pos)
        (assoc next-pos amphipod))))

(defn dist
  [from-state to-state]
  (let [from (first (filter (comp not to-state) (keys from-state)))
        to   (first (filter (comp not from-state) (keys to-state)))
        amphipod (from-state from)]
    (assert (and (not (nil? from))
                 (not (nil? to))
                 (= (from-state from) (to-state to))))
    (energy amphipod from to)))

(defn h
  [state]
  (if (hallway-deadlock? state)
    ##Inf
    (apply
     +
     (for [[pos amphipod] state]
       (cond
         (happily-in-destination-room? pos state)
         0

         ; energy to leave the room, step to the side, and return
         (in-destination-room? pos state)
         (let [[_ depth] (hallway-pos+room-depth pos)]
           (* (energy-per-step amphipod)
              (* 2 (inc depth))))

         ; energy to go into first depth of destination room
         :else
         (energy amphipod
                 pos
                 (construct-position (destiation-hallway-pos amphipod) 1)))))))

(defn parse
  [text]
  (kv->map
   (map
    vector
    (amphipod-starting-positions)
    (keep {\A 2, \B 4, \C 6, \D 8} text))))

(defn goal
  []
  (->> (repeat *room-size* "ABCD")
       (str/join)
       (parse)))

(defn route-cost
  [dist initial route]
  (apply
   +
   (map (partial apply dist)
        (partition 2 1 (cons initial route)))))

(defn solve
  [initial-state]
  (route-cost dist
              initial-state
              (astar/route graph dist h initial-state (goal))))

(def solution
  {:parse parse
   :solve solve})
