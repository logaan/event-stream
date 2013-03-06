(ns event-stack.terminal
  (:require [lanterna.screen :as s]))

(defn get-keypress! [{:keys [screen] :as input}]
  (let [keypress       (s/get-key-blocking screen)
        keypress-event {:type :keypress :key keypress}]
    (update-in input [:events] #(cons keypress-event %))))

(defn handle-exit [{[event & events] :events :as input}]
  (if (and (= (:type event) :keypress)
           (= (:key event)  \q))
    (throw (RuntimeException. "Quitting"))
    input))

(def player-char "@")

(defn draw-screen! [{screen :screen {[x y] :position} :player :as input}]
  (s/clear screen)
  (s/put-string screen x y player-char {:fg :yellow}) 
  (s/redraw screen)
  input)

(defn setup [game]
  (let [screen (s/get-screen :unix)
       new-game (assoc game :screen screen)] 
    (s/start screen)
    (draw-screen! new-game)
    new-game)) 

(defn teardown [{screen :screen}]
  (s/stop screen))

