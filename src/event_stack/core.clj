(ns event-stack.core 
  (:import jline.console.ConsoleReader
           jline.UnixTerminal))

(def codes {
  :reset   "[0m"
  :black   "[30m"
  :red     "[31m"
  :green   "[32m"
  :yellow  "[33m"
  :blue    "[34m"
  :magenta "[35m"
  :cyan    "[36m"
  :white   "[37m"})

(defn -main []
  (let [console (ConsoleReader.)]
    (.clearScreen console)
    (println "Press a key:")
    (let [letter (-> console .readCharacter char str)]
      (println (str (codes :reset) "You pressed: " (codes :magenta) letter))
      (println (str (codes :reset) "You pressed: " (codes :red) letter)))))

