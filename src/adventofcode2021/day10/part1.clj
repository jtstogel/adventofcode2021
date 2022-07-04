(ns adventofcode2021.day10.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse
  [text]
  (clojure.string/split-lines text))

(def open->closed {\{ \}, \[ \], \< \>, \( \)})

(defn process-chunk
  [{:keys [closing-chunks remaining-chunks] :as state}]
  (when-let [chunk (peek remaining-chunks)]
    (cond
      (open->closed chunk)
      (assoc state
             :closing-chunks (conj closing-chunks (open->closed chunk))
             :remaining-chunks (pop remaining-chunks))
      (= (peek closing-chunks) chunk)
      (assoc state
             :closing-chunks (pop closing-chunks)
             :remaining-chunks (pop remaining-chunks))
      :else nil)))

(defn process-chunks
  [chunks]
  (let [initial {:closing-chunks '(), :remaining-chunks (apply list chunks)}]
    (last (take-while (comp not nil?) (iterate process-chunk initial)))))

(def chunk->score {\) 3, \] 57, \} 1197, \> 25137})

(defn score-illegal
  [chunks]
  (let [{:keys [remaining-chunks]} (process-chunks chunks)]
    (when (seq remaining-chunks)
      (chunk->score (peek remaining-chunks)))))

(defn solve
  [chunks-coll]
  (->> chunks-coll
       (keep score-illegal)
       (apply +)))

(def solution
  {:parse parse
   :solve solve})
