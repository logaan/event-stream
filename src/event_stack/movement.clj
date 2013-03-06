(ns event-stack.movement)

(def keys->moves {\h :left \j :down \k :up \l :right})

(defn interpret-movement [{[event & events] :events :as game}]
  (if (= (event :type) :keypress)
    (let [direction  (keys->moves (event :key))
          move-event {:type :move :direction direction}]
      (if direction
        (assoc-in game [:events] (cons move-event events))
        game))
    game))

