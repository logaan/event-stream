(ns event-stack.movement)

(def keys->moves {\h :left \j :down \k :up \l :right})

(defn interpret-movement [{state :state [event & events] :events :as input}]
  (if (= (event :type) :keypress)
    (let [direction  (keys->moves (event :key))
          move-event {:type :move :direction direction}]
      (if direction
        (assoc-in input [:events] (cons move-event events))
        input))
    input))

