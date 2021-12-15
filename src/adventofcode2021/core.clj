(ns adventofcode2021.core
  (:require [adventofcode2021.solutions :as solutions])
  (:gen-class))

; https://stackoverflow.com/questions/62724497/how-can-i-record-time-for-function-call-in-clojure
(defmacro time-execution
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*out* s#]
       (hash-map :return (time ~@body)
                 :time-ms   (Float/parseFloat (.replaceAll (str s#) "[^0-9\\.]" ""))))))

(defn solve-part
  [day part input-file]
  (let [{:keys [parse solve]} (get-in solutions/registry [day part])
        input (->> (or input-file "input.txt")
                   (str "src/adventofcode2021/" day "/")
                   (slurp))
        {parse-time-ms :time-ms parsed :return} (time-execution (parse input))
        {solve-time-ms :time-ms solution :return} (time-execution (solve parsed))]
    {:parse-time-ms parse-time-ms
     :solve-time-ms solve-time-ms
     :solution solution
     :day day
     :part part}))

(defn format-solution
  [{:keys [solution day part parse-time-ms solve-time-ms]}]
  (format "%s, %s, parse: %.2fms, solve: %.2fms\n%s\n"
          day part parse-time-ms solve-time-ms solution))

(defn run-all-days
  []
  (doall (for [day (sort (keys solutions/registry))
               part (sort (keys (solutions/registry day)))]
           (println (format-solution (solve-part day part nil))))))

(defn run-subdir
  [[day part input-file]]
  (println (:solution (solve-part day part input-file))))

(defn -main [& args]
  ()
  (if (= (first args) "all")
    (run-all-days)
    (run-subdir args)))
