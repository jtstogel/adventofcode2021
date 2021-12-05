(ns adventofcode2021.core
  (:require [adventofcode2021.solutions])
  (:gen-class))

(defn run-subdir
  [[day part input-file]]
  (let [{:keys [parse solve]} (get-in adventofcode2021.solutions/registry [day part])]
    (println (solve (parse (slurp (str "src/adventofcode2021/" day "/" (or input-file "input.txt"))))))))

(defn -main [& args]
  ()
  (run-subdir args))
