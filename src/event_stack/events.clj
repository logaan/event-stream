(ns event-stack.events)

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

