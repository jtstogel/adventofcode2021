(ns adventofcode2021.day18.part1
  (:require [clojure.string]
            [clojure.math.numeric-tower :as math]
            [clojure.zip :as zip]))

(defn parse
  [text]
  (->> text
       (clojure.string/split-lines)
       (mapv read-string)))

(defn leaves
  "Lazily iterates through leaves of a zipper."
  [z]
  (->> (iterate zip/next z)
       (take-while (complement zip/end?))
       (filter (complement zip/branch?))))

(defn nth||nil
  [index coll]
  (nth coll index nil))

(defn edit-leaf
  "Edits a leaf node or returns the original root if the index does not exist."
  [index f root]
  (let [loc (->> (zip/vector-zip root)
                 (leaves)
                 (nth||nil index))]
    (if loc
      (zip/root (zip/edit loc f))
      root)))

(defn find-indexed
  "Returns the first [index el] such that (pred el) is truthy
   or nil if no such element exists."
  [pred coll]
  (->> coll
       (map-indexed vector)
       (filter (comp pred second))
       (first)))

(defn split
  "Splits the first leaf node with value at least 10.
   Returns nil if there was nothing to split."
  [root]
  (when-let [[_ loc] (find-indexed (comp (partial <= 10) zip/node) 
                                   (leaves (zip/vector-zip root)))]
    (->> [(math/floor (/ (zip/node loc) 2))
          (math/ceil  (/ (zip/node loc) 2))]
         (zip/make-node loc nil)
         (zip/replace loc)
         (zip/root))))

(defn pair?
  [node]
  (every-pred number? node))

(defn explode
  "Explodes the first pair with depth at least 4.
   Returns nil if there was nothing to explode."
  [root]
  (when-let [[explode-idx explode-left-loc]
             (->> (zip/vector-zip root)
                  (leaves)
                  (find-indexed #(and (pair? (zip/node (zip/up %)))
                                      (< 4 (count (zip/path %))))))]
    (let [explode-loc (zip/up explode-left-loc)
          [left right] (zip/node explode-loc)]
      (->>
       ; Replace the exploding node with 0.
       (zip/replace explode-loc 0)
       (zip/root)
       ; Update the leaves to the left and right.
       (edit-leaf (dec explode-idx) (partial + left))
       (edit-leaf (inc explode-idx) (partial + right))))))

(defn reduce-snailfish-number
  [root]
  (let [after-explode (explode root)
        after-split (split root)]
    (cond
      after-explode (recur after-explode)
      after-split (recur after-split)
      :else root)))

(defn snalfish+
  [& operands]
  (reduce #(reduce-snailfish-number [%1 %2]) operands))

(defn magnitude
  [node]
  (if (number? node)
    node
    (+ (* 3 (magnitude (first node)))
       (* 2 (magnitude (second node))))))

(defn solve
  [operands]
  (magnitude (apply snalfish+ operands)))

(def solution
  {:parse parse
   :solve solve})