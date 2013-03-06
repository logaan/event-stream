(ns event-stack.core 
  (:require
    [event-stack
     [terminal :as terminal]
     [movement :as movement]]))

(def blank-game 
  {:events []})

(def setup
  terminal/setup)

(def teardown
  terminal/teardown)

(def game-loop
  (comp terminal/draw-screen!
        movement/interpret-movement
        terminal/handle-exit
        terminal/get-keypress!))

(defn -main []
  (try
    (loop [game (setup blank-game)] 
      (recur (game-loop game)))
    (catch Exception e
      (throw e))
    (finally (teardown))))

