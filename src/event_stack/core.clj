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

(defn log [game]
  (spit "output.log" (str game "\n") :append true)
  game)

(defn game-loop [game]
  (-> game
      terminal/get-keypress!
      terminal/handle-exit
      movement/interpret-movement
      movement/move
      terminal/draw-screen!))

(defn -main []
  (let [setup-game (setup blank-game)]
    (try
      (loop [game setup-game] 
        (recur (game-loop game)))
      (catch Exception e
        (throw e))
      (finally
        (teardown setup-game)))))

