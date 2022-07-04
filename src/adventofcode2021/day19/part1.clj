(ns adventofcode2021.day19.part1
  (:require [clojure.string]
            [clojure.set]
            [clojure.math.combinatorics :as combo]))

(defn parse-int [text]
  (Integer/parseInt text))

(defn parse-coords
  [text]
  (mapv parse-int (clojure.string/split text #",")))

(defn parse-scanner
  [text]
  (->> text
       (clojure.string/split-lines)
       (drop 1)
       (mapv parse-coords)))

(defn parse
  [text]
  (->> (clojure.string/split text #"\r?\n\r?\n")
       (mapv parse-scanner)))

(defn rotate-3d
  "Returns a collection of all 3d rotations of a point about the origin."
  ([coord] (rotate-3d coord -))  ; assume numerical coordinates by default
  ([coord neg]
   (letfn [(v [[x y z]] [(neg y) x z])   ; rotation about z-axis 
           (h [[x y z]] [(neg z) y x])]  ; rotation about x-axis 
     (reductions
      (fn [x f] (f x))
      coord
      ; applying each rotation in sequence yields every 3d-rotation once
      [v v v
       (comp h v) v v v
       (comp h v) v v v
       (comp h v) v v v
       h v v v
       (comp h h) v v v]))))

(defn apply-rotation
  "Applies the rotation to a coordinate.
   The rotation is of the form [{:idx original-idx :sign sign} ...].
   original-idx is the index of the axis in vector representation (so x is 0, y is 1, z is 2).
   sign is the + or - sign that the value on that axis should take.
   Each dict indicates where that signed axis should go in the rotated coordinate.
   For example, [{:idx 2 :sign 1} {:idx 0 :sign -1} {:idx 1 :sign 1}]
   indicates the transformation [x y z] -> [z -x y]."
  [rotation coord]
  (mapv
   (fn [axis] (* (:sign axis) (coord (:idx axis))))
   rotation))

(def rotations-3d
  "Every 3d 90-deg rotation, as accepted by the apply-rotation function."
  (vec (letfn [(negate [axis] (update axis :sign -))]
         (rotate-3d
          [{:idx 0 :sign 1} {:idx 1 :sign 1} {:idx 2 :sign 1}]
          negate))))

(def identity-rotation (rotations-3d 0))

(defn kv->map [kv-pairs]
  (apply hash-map (apply concat kv-pairs)))

(defn inv-rotation-fn
  [rot]
  (first (filter #(->> (apply-rotation rot [1 2 3])
                       (apply-rotation %)
                       (= [1 2 3]))
                 rotations-3d)))

(def inv-rotation  ; find the inverse of a rotation
  (kv->map (for [rot rotations-3d]
             [rot (inv-rotation-fn rot)])))

(defn comp-rotation-fn
  [rot-f rot-g]
  (let [cmp (apply-rotation rot-f (apply-rotation rot-g [1 2 3]))]
    (->> rotations-3d
         (filter #(= cmp (apply-rotation % [1 2 3])))
         (first))))

(def comp-rotation  ; compose two rotations
  (kv->map (for [rot-f rotations-3d
                 rot-g rotations-3d]
             [[rot-f rot-g] (comp-rotation-fn rot-f rot-g)])))

(def sub-tuple (partial mapv -))
(def add-tuple (partial mapv +))

(defn beacon-overlap-no-rotation
  "Computes the offset and common beacons of two scanners based on their relative beacon measurements.
   The 'offset' between s0 and s1 is a 3-tuple s.t. `s0 = s1 - offset`."
  [s0-beacons-set s1-beacons]
  ; Since we need 12 matches, we can just skip the first 11 beacons when generating offsets.
  ; The desired offset must still be in the generated set.
  (let [offsets (set (for [b0 (drop 11 s0-beacons-set)  
                           b1 s1-beacons]
                       (sub-tuple b1 b0)))]
    (->> (for [offset offsets]
           (let [offset-b1-beacons (map #(sub-tuple % offset) s1-beacons)
                 common-beacon-count (count (filter s0-beacons-set offset-b1-beacons))]
             (when (>= common-beacon-count 12)
               {:offset offset})))
         (filter (comp not nil?))
         (first))))

(defn beacon-overlap
  [s0-beacons s1-beacons]
  (letfn [(rotate-all-beacons [rot beacons] (mapv (partial apply-rotation rot) beacons))]
    (->> rotations-3d
         (keep (fn [rot]
                 (when-let [overlap (beacon-overlap-no-rotation (set s0-beacons)
                                                                (rotate-all-beacons rot s1-beacons))]
                   (assoc overlap :rotation rot))))
         (first))))

(defn indexed
  "Returns a collection of [index item] vectors."
  [coll]
  (map-indexed vector coll))

(defn find-overlapping-beacons
  [scanners]
  (filterv
   (comp not nil?)
   (for [[[s0-idx s0] [s1-idx s1]] (combo/combinations (indexed scanners) 2)]
     (when-let [overlap (beacon-overlap s0 s1)]
       {s0-idx {:rotation identity-rotation
                :offset [0 0 0]}
        s1-idx {:rotation (:rotation overlap)
                :offset (:offset overlap)}}))))

(defn find-common-scanner
  [g0 g1]
  (when-let [common (seq (clojure.set/intersection
                          (set (keys g0))
                          (set (keys g1))))]
    (apply min common)))

(defn transform-scanner
  [{:keys [rotation offset]} coord]
  (add-tuple (apply-rotation rotation coord) offset))

(defn transform-point
  [{:keys [rotation offset]} coord]
  (sub-tuple (apply-rotation rotation coord) offset))

(defn transform-scanner-group
  "Tranforms all scanners within a group according to the passed transformation."
  [transform g]
  (kv->map (for [[sidx g-transform] g]
             [sidx {:rotation (comp-rotation [(:rotation transform) (:rotation g-transform)]) 
                    :offset (transform-scanner transform (:offset g-transform))}])))

(defn center-group
  "Centers a group around a specific scanner."
  [group scanner-idx]
  (let [{:keys [rotation offset]} (group scanner-idx)
        inv-rot (inv-rotation rotation)]
    (kv->map (for [[sidx transform] group]
               [sidx {:rotation (comp-rotation [inv-rot (:rotation transform)])
                      :offset (apply-rotation inv-rot (sub-tuple (:offset transform) offset))}]))))

(defn combine-scanner-group-pair
  "Assimilate one scanner group into the other, if possible.
   Else return nil."
  [g0 g1]
  (when-let [common-scanner (find-common-scanner g0 g1)]
    ; choose g0 to be the "origin" group
    (let [g0-transform (g0 common-scanner)
          centered-g1 (center-group g1 common-scanner)]
      (merge g0 (transform-scanner-group g0-transform centered-g1)))))

(defn combine-scanner-groups
  [groups]
  (loop [groups (set groups)]
    (if (= (count groups) 1)
      (first groups)
      (when-let [{:keys [groups-combined combined-group]}
                 (->> (for [pair (combo/combinations groups 2)]
                        {:groups-combined (set pair)
                         :combined-group (apply combine-scanner-group-pair pair)})
                      (filter :combined-group)
                      (first))]
        (recur (-> groups
                   (clojure.set/difference groups-combined)
                   (clojure.set/union #{combined-group})))))))

(defn solve
  [scanners]
  (let [scanner-groups (find-overlapping-beacons scanners)
        joined-scanners (combine-scanner-groups scanner-groups)] 
    (->> (indexed scanners)
         (mapcat
          (fn [[scanner-idx beacons]]
            (map (partial transform-point (joined-scanners scanner-idx)) beacons)))
         (set)
         (count))))

(def solution
  {:parse parse
   :solve solve})
