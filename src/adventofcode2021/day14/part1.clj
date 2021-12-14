(ns adventofcode2021.day14.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-rule
  [text]
  (let [[pair insert] (clojure.string/split text #" -> ")]
    {(vec (map str (seq pair))) insert}))

(defn parse-rules
  [text]
  (apply merge (map parse-rule (clojure.string/split-lines text))))

(defn parse
  [text]
  (let [[template rules-text] (clojure.string/split text #"\n\n")]
    {:template (vec (map str (seq template)))
     :rules (parse-rules rules-text)}))

(defn pairs [coll]
  (partition 2 1 coll))

(defn new-pairs
  [rules [[a b :as pair] count]]
  (if-let [insertion (rules pair)]
    [{[a insertion] count}
     {[insertion b] count}]
    [{pair count}]))

(defn apply-insertion-rules
  [rules pair-counts]
  (apply merge-with + (flatten (keep (partial new-pairs rules) pair-counts))))

(defn apply-insertion-rules-repeatedly
  [n rules pair-counts]
  (nth (iterate (partial apply-insertion-rules rules) pair-counts) n))

(defn element-counts
  [first-el last-el pair-counts]
  (->> pair-counts
       (mapcat (fn [[[a b] count]] [{a count} {b count}]))
       ; Even after many insertions, the first element and last element won't change.
       ; Below we'll double count every element (since we're summing all the pairs),
       ; so to account for the endpoints, we add +1 for each endpoint element.
       (apply merge-with + {first-el 1} {last-el 1})
       (mapcat (fn [[c doubled-count]] [c (quot doubled-count 2)]))
       (apply hash-map)))

(defn solve-n
  [apply-times template rules]
  (let [template-pair-counts (frequencies (map vec (pairs template)))
        pair-counts (apply-insertion-rules-repeatedly apply-times rules template-pair-counts)
        element-counts (element-counts (first template) (last template) pair-counts)
        counts (map second element-counts)]
    (- (apply max counts)
       (apply min counts))))

(defn solve
  [{:keys [template rules]}]
  (solve-n 10 template rules))

(def solution
  {:parse parse
   :solve solve})
