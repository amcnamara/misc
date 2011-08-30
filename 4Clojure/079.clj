;; See http://www.4clojure.com/problem/79

;; Write a function which calculates the sum of the minimal path through a triangle. The
;; triangle is represented as a vector of vectors. The path should start at the top of
;; the triangle and move to an adjacent number on the next row until the bottom of the triangle
;; is reached.

;; NOTE: Uses binary pruning of first and last items at each depth and bubbles up a minimal path.

(defn tri [coll]
  (let [head (first (first coll))]
    (if (= 1 (count coll))
      head
      (+ head (min (tri (rest (map rest coll)))
                   (tri (rest (map butlast coll))))))))

;; Golf score: 92 (minified)
(fn f [c]
  (let [a first
        b rest
        n (-> c a a)]
    (if (= 1 (count c))
      n
      (+ n (min (f (b (map b c)))
                (f (b (map butlast c))))))))