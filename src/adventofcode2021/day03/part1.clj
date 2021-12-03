(ns adventofcode2021.day03.part1
  (:require [clojure.string]))

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

(defn power-consumption
  [{:keys [gamma-rate epsilon-rate]}]
  (* (binary-seq-to-int gamma-rate)
     (binary-seq-to-int epsilon-rate)))

(defn compute-gamma-epislon-rates
  [binary-nums]
  (->> binary-nums
       (transpose)
       (map frequencies)
       ((fn [freqs]
          {:gamma-rate (map #(max-key % 0 1) freqs)
           :epsilon-rate (map #(min-key % 0 1) freqs)}))))

(defn solve
  [binary-nums]
  (->> binary-nums
       (compute-gamma-epislon-rates)
       (power-consumption)))

(def solution
  {:parse parse
   :solve solve})
