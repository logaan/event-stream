(ns event-stack.core 
  (:require
    [event-stack
     [terminal :as terminal]
     [movement :as movement]]))

(def game-loop
  (comp terminal/draw-screen!
        movement/interpret-movement
        terminal/handle-exit
        terminal/get-keypress!))

(defn -main []
  (try
    (loop [thread (terminal/setup)] 
      (recur (game-loop thread)))
    (catch Exception e
      (throw e))
    (finally (terminal/teardown))))

