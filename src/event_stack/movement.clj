(ns event-stack.movement)

(defn setup [game]
  (assoc game :player {:position [10 10]}))

(def keys->moves
  {\h [:left]
   \j [:down]
   \k [:up] 
   \l [:right]
   
   \y [:up   :left]
   \u [:up   :right]
   \b [:down :left]
   \n [:down :right]})

(def moves->offsets
  {:left  [-1 0]
   :right [+1 0]
   :up    [0 -1]
   :down  [0 +1]})

(defn move-event [direction]
  {:type :move :direction direction})

(defn is-event? [type]
  (fn [event]
    (= (:type event) type)))

(defn handle-events [{events :events :as game} event-type handling-function]
  (let [{relevant-events true other-events false} (group-by (is-event? event-type) events)]
    (-> (reduce handling-function game relevant-events)
        (assoc :events other-events))))

(defn add-events [{events :events :as game} events]
  (assoc-in game [:events] (concat move-events events)))

(defn convert-keypress-event [game keypress-event]
  (let [directions  (keys->moves (keypress-event :key))
        move-events (vec (map move-event directions))]
    (if (not (empty? directions))
      (add-events game move-events) 
      game)))

(defn move-player [{{position :position} :player :as game} {direction :direction}]
  (let [offsets      (moves->offsets direction)
        new-position (vec (map + position offsets))]
    (assoc-in game [:player :position] new-position)))

(defn interpret-movement [game]
  (handle-events game :keypress convert-keypress-event)) 

(defn move [game]
  (handle-events game :move move-player))

(move {:player {:position [10 10]}
       :events [{:type :move :direction :down}]})

(move {:player {:position [10 10]}
       :events []})

