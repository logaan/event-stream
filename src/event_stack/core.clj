;; The core ns exists to compose together all the pieces of the game at a high
;; level and set them in motion.
(ns event-stack.core 
  (:require
    [event-stack
     [util     :as util] 
     [terminal :as terminal]
     [movement :as movement]]))

;; This is the bare minimum game. Any specifics should be added by setup
;; functions.
(def blank-game 
  {:events []})

(defn setup!
  "Prepares a blank game for start of play."
  [game]
  (-> game
      movement/setup  
      terminal/setup))

(defn cleanup!
  "Closes and finalises anything opened during startup."
  [game]
  (-> game
      terminal/teardown))

(defn event-sources!
  "Provides events from the outside world such as user input."
  [game]
  (-> game
      terminal/get-keypress!))

(defn event-handlers
  "Purely functional core. It should take any events and process them. These
  handlers may emit events of their own and will continue to be run until all
  events have been consumed."
  [game]
  (-> game
      terminal/handle-exit
      movement/interpret-movement
      movement/move-direction-to-move-position
      movement/prevent-movement-outside-boundaries
      movement/update-position))

(defn update-views!
  "Views are any side-effecty code that wants to know about the state of the
  game. They will only be updated once all internal event firing has died dow,
  and so they should present a consistent view of the world."
  [game]
  (-> game
      terminal/draw-screen!))

(defn process-events
  "Runs event-handlers until they have consumed all new events and stopped
  emitting events of their own."
  [game]
  (if (empty? (:events game)) 
    game (recur (event-handlers game))))

(defn process-input
  "The outer game loop. It takes events from the outside world, runs them
  through our functional core and then notifies views of the latest state."
  [game]
  (recur (-> game event-sources! process-events update-views!)))

(defn -main
  "Handles setup, starts the game loop and ensures any cleanup occurs."
  []
  (let [setup-game (setup! blank-game)]
    (try
      (process-input setup-game)
      (catch Exception e
        (throw e))
      (finally
        (cleanup! setup-game)))))

