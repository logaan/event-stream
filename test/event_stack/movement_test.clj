(ns event-stack.movement-test
  (:use midje.sweet 
        event-stack.movement))

(fact
  (move-direction-to-move-position {:player {:position [10 10]}
                                    :events [{:type :move :direction :down}]})
  => {:events [{:position [10 11], :type :move-to}], :player {:position [10 10]}})

(fact
  (move-direction-to-move-position {:player {:position [10 10]} :events []})
  => {:player {:position [10 10]} :events []})

(fact
  (interpret-movement {:events [{:type :keypress :key \u}]})
  => {:events [{:type :move, :direction :up} {:type :move, :direction :right}]})

(fact
  (interpret-movement {:events [{:type :keypress :key \h}]})
  => {:events [{:type :move, :direction :left}]}) 

(fact
  (interpret-movement {:events [{:type :keypress :key \t}]})
  => {:events []}) 

(fact
  (update-position {:player {:position [10 10]}
                  :events [{:type :move-to :position [20 20]}]})
  => {:player {:position [20 20]}, :events nil})

