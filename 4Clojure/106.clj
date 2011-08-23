;; See http://www.4clojure.com/problem/106

;; Given a pair of numbers, the start and end point, find a path between the two using only three possible operations:
;;  - double
;;  - halve (odd numbers cannot be halved)
;;  - add 2
;; Find the shortest path through the "maze". Because there are multiple shortest paths, you must return the length of the shortest path, not the path itself.

(fn [start end]
  (loop [current [start] steps 1]
    (if (some #{end} current)
      steps
      (recur (flatten
              (map #(if %
                      (list (* 2 %)
                            (+ % 2)
                            (if (even? %)
                              (/ % 2))))
                   current))
	     (+ 1 steps)))))


;; Golf score: 104 (minified)
(fn [s e]
  (loop [c [s] i 1]
    (if (some #{e} c)
      i
      (recur (flatten
              (map #(if %
                      (list (* 2 %)
                            (+ 2 %)
                            (if (even? %)
                              (/ % 2))))
                   c))
             (+ 1 i)))))