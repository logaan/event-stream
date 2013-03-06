(ns event-stack.terminal
  (:require [lanterna.screen :as s]))

(defn setup [game]
  (let [screen (s/get-screen :unix)
       new-game (assoc game :screen screen)] 
    (s/start screen)
    new-game)) 

(defn teardown [{screen :screen}]
  (s/stop screen))

(defn get-keypress! [{:keys [screen] :as input}]
  (let [keypress       (s/get-key-blocking screen)
        keypress-event {:type :keypress :key keypress}]
    (update-in input [:events] #(cons keypress-event %))))

(defn handle-exit [{[event & events] :events :as input}]
  (if (and (= (:type event) :keypress)
           (= (:key event)  \q))
    (throw (RuntimeException. "Quitting"))
    input))

(defn draw-screen! [{screen :screen [event & events] :events :as input}]
  (if (= (:type event) :move)
    (let [direction (:direction event)
          output    (str "You move " (name direction))]
      (s/clear screen)
      (s/put-string screen 10 10 output {:fg :black :bg :yellow}) 
      (s/redraw screen)
      (update-in input [:events] rest))
    input))

