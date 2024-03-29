(ns adventofcode2021.day14.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse-rule
  [text]
  (let [[pair insert] (clojure.string/split text #" -> ")]
    {(vec (map str pair)) insert}))

(defn parse-rules
  [text]
  (apply merge (map parse-rule (clojure.string/split-lines text))))

(defn parse
  [text]
  (let [[template rules-text] (clojure.string/split text #"\r?\n\r?\n")]
    {:template (vec (map str template))
     :rules (parse-rules rules-text)}))

(defn pairs [coll]
  (partition 2 1 coll))

(defn apply-n-times
  [n f v]
  (nth (iterate f v) n))

(defn new-pairs
  [rules [[a b :as pair] count]]
  (if-let [insertion (rules pair)]
    [{[a insertion] count}
     {[insertion b] count}]
    [{pair count}]))

(defn apply-insertion-rules
  [rules pair-counts]
  (->> pair-counts
       (keep (partial new-pairs rules))
       (flatten)
       (apply merge-with +)))

(defn element-counts
  [template pair-counts]
  (->> pair-counts
       ; To avoid double counting, we'll only count the first of each pair.
       (map (fn [[[a _] count]] {a count}))
       ; ...however, this will leave the endpoint element deficient.
       ; Even after many insertions, the first last element won't change,
       ; so we just correct that here:
       (apply merge-with + {(last template) 1})))

(defn solve-n
  [times template rules]
  (let [template-pair-counts (frequencies (map vec (pairs template)))
        pair-counts (apply-n-times times (partial apply-insertion-rules rules) template-pair-counts)
        element-counts (element-counts template pair-counts)]
    (- (apply max (vals element-counts))
       (apply min (vals element-counts)))))

(defn solve
  [{:keys [template rules]}]
  (solve-n 10 template rules))

(def solution
  {:parse parse
   :solve solve})
