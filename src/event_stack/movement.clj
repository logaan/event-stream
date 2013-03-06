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
  (let [grouped-events (group-by (is-event? event-type) events)
        {relevant-events true other-events false} grouped-events
        uneventful-game (assoc game :events other-events)]
    (reduce handling-function uneventful-game relevant-events)))

(defn convert-keypress-event [game keypress-event]
  (let [directions  (keys->moves (:key keypress-event))
        move-events (vec (map move-event directions))]
    (update-in game [:events] (partial concat move-events))))

(defn within-the-boundaries? [[x y]]
  (and (<= 0 x 79) (<= 0 y 23)))

(defn move-player [{{position :position} :player :as game} event]
  (let [offsets      (moves->offsets (:direction event))
        new-position (vec (map + position offsets))]
    (if (within-the-boundaries? new-position)
      (assoc-in game [:player :position] new-position)
      game)))

(defn interpret-movement [game]
  (handle-events game :keypress convert-keypress-event)) 

(defn move [game]
  (handle-events game :move move-player))

; So I wanted to have a function that would consume any events that would cause
; you to walk outside of the terminal. But the move step can consume multiple
; walk events. Potentially none of which individually would step you outside,
; but combined they would. So this interceptor function would have to consume
; them all and know how they'd affect everyting. At that point it's
; implementation is so tied to the other one that I'm not sure there's any
; point.

; (move {:player {:position [10 10]}
;        :events [{:type :move :direction :down}]})
; 
; (move {:player {:position [10 10]}
;        :events []})
; 
; (interpret-movement {:events [{:type :keypress :key \u}]})
; 
; (interpret-movement {:events [{:type :keypress :key \h}]})
; 
; (interpret-movement {:events [{:type :keypress :key \t}]})

