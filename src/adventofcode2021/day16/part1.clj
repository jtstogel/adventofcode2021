(ns adventofcode2021.day16.part1
  (:require [clojure.string]
            [clojure.set]))

(defn parse
  [text]
  text) ; nothing to parse!

(defn lpad
  [n pad coll]
  (let [pad-count (max 0 (- n (count coll)))]
    (concat (repeat pad-count pad) coll)))

(def hex-char->bin
  "Map from a hex character to its corresponding binary half-byte."
  (letfn [(dec->bin [n] (lpad 4 "0" (map str (Long/toString n 2))))]
    (->> "0123456789ABCDEF"
         (map-indexed #(vector %2 (dec->bin %1)))
         (apply concat)
         (apply hash-map))))

(defn hex->bin
  [hex]
  (vec (mapcat hex-char->bin hex)))

(defn bin->dec
  "Returns the decimal representation of the provided bits."
  [bits]
  (Long/parseLong (clojure.string/join bits) 2))

; https://stackoverflow.com/questions/30921066/clojure-take-while-to-include-last-item-when-predicate-is-false
(defn take-while+
  [pred coll]
  (lazy-seq
   (when-let [[f & r] (seq coll)]
     (if (pred f)
       (cons f (take-while+ pred r))
       [f]))))

(def parse-packet)

(defn parse-int-literal-pkt-content
  "Parses an int and returns the unparsed bits."
  [bits]
  (let [sections (->> (partition 5 bits)
                      (take-while+ (comp (partial = "1") first)))]
    {:int-literal (bin->dec (mapcat rest sections))
     :unconsumed-bits (subvec bits (* 5 (count sections)))}))

(defn parse-repeatedly
  [bits]
  (if (empty? bits)
    nil
    (let [pkt (parse-packet bits)]
      (cons pkt (lazy-seq (parse-repeatedly (:unconsumed-bits pkt)))))))

(defn parse-len-type-0-operator-pkt-content
  [bits]
  (let [content-len (bin->dec (subvec bits 0 15))
        content (subvec bits 15)]
    {:subpackets (vec (parse-repeatedly (subvec content 0 content-len)))
     :unconsumed-bits (subvec content content-len)
     :content-len content-len}))

(defn parse-len-type-1-operator-pkt-content
  [bits]
  (let [subpacket-count (bin->dec (subvec bits 0 11))
        content (subvec bits 11)
        subpackets (take subpacket-count (parse-repeatedly content))]
    {:unconsumed-bits (if (empty? subpackets) [] (:unconsumed-bits (last subpackets)))
     :subpackets subpackets
     :subpacket-count subpacket-count}))

(defn parse-operator-pkt-content
  [bits]
  (let [len-type (bits 0)
        content (subvec bits 1)]
    (merge
     {:len-type len-type}
     (if (= len-type "1")
       (parse-len-type-1-operator-pkt-content content)
       (parse-len-type-0-operator-pkt-content content)))))

(defn parse-packet
  "Parses a packet and returns the unparsed bits."
  [bits]
  (let [version (bin->dec (subvec bits 0 3))
        type-id (bin->dec (subvec bits 3 6))
        content-bits (subvec bits 6)]
    (merge
     {:version version :type-id type-id}
     (if (= type-id 4)
       (parse-int-literal-pkt-content content-bits)
       (parse-operator-pkt-content content-bits)))))

(defn versions
  [pkt]
  (apply concat
         [(pkt :version)]
         (map versions (pkt :subpackets))))

(defn solve
  [hex]
  (apply + (versions (parse-packet (hex->bin hex)))))

(def solution
  {:parse parse
   :solve solve})