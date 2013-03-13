(ns event-stack.movement
  (:require
    [midje.sweet :refer :all]))

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

(defn is-event? [type event]
  (= (:type event) type))

(defn handle-event [{[event & future-events] :events :as game} event-type handler]
  (if (is-event? event-type event)
    (let [eventless-game (assoc game :events future-events)]
      (handler eventless-game event))
    game))

(defn add-events [game events]
  (update-in game [:events] (partial concat (vec events))))

(defn add-event [game event]
  (add-events game [event]))

(defn move-event [direction]
  {:type :move :direction direction})

(defn interpret-movement [game]
  (handle-event game :keypress
    (fn [game keypress-event]
      (let [directions (keys->moves (:key keypress-event))]
        (add-events game (map move-event directions)))))) 

(defn move-to-event [position]
  {:type :move-to :position position})

(defn move-direction-to-move-position [game]
  (handle-event game :move
    (fn [{{position :position} :player :as game} {direction :direction}]
      (let [offsets      (moves->offsets direction)
            new-position (vec (map + position offsets))]
          (add-event game (move-to-event new-position))))))

(defn within-the-boundaries? [[x y]]
  (and (<= 0 x 79) (<= 0 y 23)))

(defn prevent-movement-outside-boundaries [game]
  (handle-event game :move-to
    (fn [game {position :position :as event}]
      (if (within-the-boundaries? position)
        (add-event game event) game))))

(defn update-position [game]
  (handle-event game :move-to
   (fn [game {position :position}]
     (assoc-in game [:player :position] position))))

