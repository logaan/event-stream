(ns event-stack.core 
  (:require [lanterna.screen :as s]))

(defn get-keypress! [{:keys [state] :as input}]
  (let [keypress       (s/get-key-blocking (:screen state))
        keypress-event {:type :keypress :key keypress}]
    (update-in input [:events] #(cons keypress-event %))))

(def keys->moves {\h :left \j :down \k :up \l :right})

(defn interpret-movement [{state :state [event & events] :events :as input}]
  (if (= (event :type) :keypress)
    (let [move-event {:type :move :direction (keys->moves (event :key))}]
      (assoc-in input [:events] (cons move-event events)))
    input))

(defn draw-screen! [{state :state [event & events] :events :as input}]
  (let [screen    (state :screen)
        direction (:direction event)
        output    (str "You move " (name direction))]
    (s/in-screen screen
      (s/put-string screen 10 10 output {:fg :black :bg :yellow}) 
      (s/redraw screen))))

(defn -main []
  (let [screen (s/get-screen :unix)
        thread {:state {:screen screen} :events []}]
    (s/in-screen screen
      (-> thread
          get-keypress!
          interpret-movement
          draw-screen!))))

