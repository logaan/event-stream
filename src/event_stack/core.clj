(ns event-stack.core 
  (:require
    [event-stack
     [util     :as util] 
     [terminal :as terminal]
     [movement :as movement]]))

(def blank-game 
  {:events []})

(defn setup [game]
  (-> game
      movement/setup  
      terminal/setup))

(defn teardown [game]
  (-> game
      terminal/teardown))

(defn event-sources! [game]
  (-> game
      terminal/get-keypress!))

(defn event-handlers [game]
  (-> game
      terminal/handle-exit
      movement/interpret-movement
      movement/move-direction-to-move-position
      movement/prevent-movement-outside-boundaries
      movement/update-position))

(defn event-sinks! [game]
  (-> game
      terminal/draw-screen!))

(defn process-events [game]
  (if (empty? (:events game)) 
    game (recur (event-handlers game))))

(defn process-input [game]
  (recur (-> game event-sources! process-events event-sinks!)))

(defn -main []
  (let [setup-game (setup blank-game)]
    (try
      (process-input setup-game)
      (catch Exception e
        (throw e))
      (finally
        (teardown setup-game)))))

