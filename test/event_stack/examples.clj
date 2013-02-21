; When you're writing a computer game you'll have a lot of complex domain
; logic. These things can make decisions about basically anything in your game
; state. Embedding this logic into code that handles your inputs will quickly
; become confusing.
;
; By decoupling the logic from the execution using an event system we can
; isolate the logic. This allows us to turn on and off parts of our game by
; simply adding and removing their registration as listeners.
;
; Having functional event listeners should give us the usual benefits. They'll
; be easy to reason about even in a concurrent environment, they'll be easy to
; test in isolation and we know they won't be clobbering each other's data.
;
; Individual listeners should also be able to be bundled together into
; packages. So you can turn off all door logic, or all traps in your game with
; one sweep.
;
; The following code hopes to serve as an example implementation. The game
; state, events and listeners are all available to an individual listener. I
; can conceive of different types of listener that could conform to a
; simplified interface, but this one will demonstrate the most advanced api
; usage.
;
; It's worth noting that a listener is passed the full event collection.  This
; means that you could decide to act after looking at several events. For
; instance you could look for a click event, then sum all time-passing events
; between it and the next click event. If the time passing is sufficiently
; brief you could identify that as a double click and emit that event. Other
; code could simply listen for double click events and react to those, never
; having to be aware of the specifics. 
;
; One issue that could arrise is the event stream growing too large. One
; strategy for managing this would be to add some meta-data to each event as it
; is passed through the listeners. If it hits some configurable number of
; passes without being consumed then the system would remove it. Another system
; could be to watch the stream and once it grows past a certain size then drop
; the trailing events. Both of these strategies could be implemented as normal
; listeners with no special privledges. 
;
; I've read very little about Functional Reactive Programming. But I wonder if
; this perhaps fits the bill (though I accept that it's not as sexy as defining
; a graph of dependancies and having data automatically flow through).
(ns event-stack.examples
  (require [matchure :refer :all]))

; Setup

(defmacro deflistener [listener-name docstring args & body]
  `(def ~listener-name {
    :name ~(keyword listener-name)
    :doc ~docstring
    :callback (fn-match ~args
      ~@body)}))

; Fake business logic

(defn move [player direction]
  (println "called move"))

(defn unlocked? [door]
  (println "called unlocked?"))

(defn has-key? [player door]
  (println "called has-key?"))

; Example listener

(deflistener unlock-door
  "Listens for players trying to move through doors. If the door is locked
  unlock-door will unlock the door. If the door is locked movement will be
  cancelled."
  [(and ?state  {:player ?player :doors ?doors})
   (and ?events [{:type :move :direction ?direction}])
   ?listeners]

  (let [next-position (move player direction)
        door          (doors next-position)]
    (if (unlocked? door)
      [state events listeners]
      (if (has-key? player door)
        [state (cons [:unlocked :red :door] events) listeners]
        [state (rest events) listeners]))))

; Call with example inputs

(let [world     {:player "Logan" :doors {[2 2] :red}} 
      events    [{:type :move :direction :north}] 
      listeners [unlock-door]]
  ((unlock-door :callback) world events listeners)) 

