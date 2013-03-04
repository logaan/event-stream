(ns event-stack.core 
  (:import jline.console.ConsoleReader))

(defn -main []
  (println "Press a key:")
  (let [charNum (.readCharacter (ConsoleReader.))]
    (println (char charNum)))) 

