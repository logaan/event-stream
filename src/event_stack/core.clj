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

(defn event-sources! [game]
  (-> game
      terminal/get-keypress!))

(defn event-handlers [game]
  (-> game
      terminal/handle-exit
      movement/interpret-movement
      movement/move))

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

