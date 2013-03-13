(ns event-stack.util)

(defn log [game]
  (spit "output.log" (str game "\n") :append true)
  game)

