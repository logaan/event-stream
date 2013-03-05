(ns event-stack.core 
  (:require [lanterna.screen :as s]))

(defn -main []
  (println "foo")
  (let [scr (s/get-screen :unix)]
    (s/in-screen scr

    (s/put-string scr 10 10 "Hello, world!" {:fg :black :bg :yellow}) 
    (s/put-string scr 10 11 "Press any key to exit!") 

    (s/redraw scr) 

    (s/get-key-blocking scr))))

