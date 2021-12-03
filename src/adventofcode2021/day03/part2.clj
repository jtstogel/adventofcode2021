(ns adventofcode2021.day03.part2
  (:require [clojure.string]
            [clojure.set]))

(defn parse
  [text]
  (->> text
       (clojure.string/split-lines)
       (map #(clojure.string/split % #""))
       (map (partial map #(Integer/parseInt %)))))

(defn transpose
  [seqs]
  (apply map vector seqs))

(defn binary-seq-to-int
  "Converts a sequence of {0,1} to a decimal number."
  [seq]
  (Integer/parseInt (apply str seq) 2))

(defn life-support-rating
  [{:keys [oxygen-generator-rating co2-scrubber-rating]}]
  (* (binary-seq-to-int oxygen-generator-rating)
     (binary-seq-to-int co2-scrubber-rating)))

(defn get-or-default [not-found m k] (get m k not-found))

(defn compute-gamma-epislon-rates
  [binary-nums]
  (->> binary-nums
       (transpose)
       (map frequencies)
       ((fn [freqs]
          ; The tie-breaker is encoded by {max,min}-key using the last key for ties.
          {:gamma-rate (map #(max-key (partial get-or-default 0 %) 0 1) freqs)
           :epsilon-rate (map #(min-key (partial get-or-default 0 %) 1 0) freqs)}))))

(defn narrow-solutions
  [criteria-key bit-idx binary-nums]
  (let [bit-criteria (get (compute-gamma-epislon-rates binary-nums) criteria-key)]
    (filter #(= (nth % bit-idx) (nth bit-criteria bit-idx))
            binary-nums)))

(defn compute-rating
  [criteria-key binary-nums]
  (loop [bit-idx 0
         nums binary-nums]
    (if (= 1 (count nums))
      (first nums)
      (recur (inc bit-idx)
             (narrow-solutions criteria-key bit-idx nums)))))

(defn solve
  [binary-nums]
  (let [ratings {:oxygen-generator-rating (compute-rating :gamma-rate binary-nums)
                 :co2-scrubber-rating (compute-rating :epsilon-rate binary-nums)}]
    (life-support-rating ratings)))

(def solution
  {:parse parse
   :solve solve})
