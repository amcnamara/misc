;; See http://www.4clojure.com/problem/73

;; A tic-tac-toe board is represented by a two dimensional vector. X is represented
;; by :x, O is represented by :o, and empty is represented by :e. A player wins by
;; placing three Xs or three Os in a horizontal, vertical, or diagonal row. Write a
;; function which analyzes a tic-tac-toe board and returns :x if X has won, :o if O
;; has won, and nil if neither player has won.

(fn [b]
  (first (filter #(and % (not= % :e))
    (map #(if (= 1 (count (group-by identity %))) (first %))
          (concat b (partition 3 (apply interleave b))
                    (vector (map #(nth (flatten b) %) [0 4 8]))
                    (vector (map #(nth (flatten b) %) [2 4 6])))))))


;; Golf score: 150
(fn [b]
  (first (filter #(not= % :e)
    (map #(if (= 1 (count (set %))) (nth % 0) :e)
          (concat b (partition 3 (apply interleave b))
                    (for [i [[0 4 8][2 4 6]]]
                      (map #(nth (flatten b) %) i)))))))