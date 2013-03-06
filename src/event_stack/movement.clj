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

(defn move-event [direction]
  {:type :move :direction direction})

(defn interpret-movement [{[event & events] :events :as game}]
  (if (= (event :type) :keypress)
    (let [directions  (keys->moves (event :key))
          move-events (map move-event directions)]
      (if (not (empty? directions))
        (assoc-in game [:events] (concat move-events events))
        game))
    game))

(def moves->offsets
  {:left  [-1 0]
   :right [+1 0]
   :up    [0 +1]
   :down  [0 -1]})

(defn move [{[event & events] :events player :player :as game}]
  (if (= (:type event) :move)
    (let [offsets      (moves->offsets (:direction event))
          new-position (map + (:position player) offsets)]
      (-> game
          (assoc-in [:events] events)
          (assoc-in [:player :position] new-position)))
    game))


(move {:player {:position [10 10]}
       :events [{:type :move :direction :down}]})

(move {:player {:position [10 10]}
       :events []})
