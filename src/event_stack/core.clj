(ns event-stack.core 
  (:require
    [event-stack
     [terminal :as terminal]
     [movement :as movement]]))

(def blank-game 
  {:events []})

(def setup
  (comp terminal/setup
        movement/setup))

(def teardown
  terminal/teardown)

(def game-loop
  (comp terminal/draw-screen!
        movement/interpret-movement
        terminal/handle-exit
        terminal/get-keypress!))

(defn -main []
  (let [setup-game (setup blank-game)]
    (try
      (loop [game setup-game] 
        (recur (game-loop game)))
      (catch Exception e
        (throw e))
      (finally
        (teardown setup-game)))))

