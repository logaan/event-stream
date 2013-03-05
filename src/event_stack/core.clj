(ns event-stack.core 
  (:require
    [event-stack
     [terminal :as terminal]
     [movement :as movement]]))

(defn game-loop [thread]
  (-> thread
      terminal/get-keypress!  
      terminal/handle-exit
      movement/interpret-movement
      terminal/draw-screen!))

(defn -main []
  (try
    (loop [thread (terminal/setup)] 
      (recur (game-loop thread)))
    (catch Exception e
      (throw e))
    (finally (terminal/teardown))))

