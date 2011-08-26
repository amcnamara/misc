;; See http://www.4clojure.com/problem/94

;; The game of life is a cellular automaton devised by mathematician John Conway.

;; The 'board' consists of both live (#) and dead ( ) cells. Each cell interacts with its eight
;; neighbours (horizontal, vertical, diagonal), and its next state is dependent on the following rules:

;;   1) Any live cell with fewer than two live neighbours dies, as if caused by under-population.
;;   2) Any live cell with two or three live neighbours lives on to the next generation.
;;   3) Any live cell with more than three live neighbours dies, as if by overcrowding.
;;   4) Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

;; Write a function that accepts a board, and returns a board representing the next generation of cells.

;; NOTE: This script ignores elements along the outside edge (fewer than 9 neighbours), the conditionals
;;       to parse those elements were omitted to save space in the solution (and don't matter for the
;;       test cases).  To complete the game mechanic change the s predicate to include edge cases, or
;;       tuple the element value with the neighbour-value collections from parse-neighbours.

(fn game-of-life [board]
  (let [parse-neighbours #(for [y (range 0 (count board))
                                x (range 0 (count (first board)))]
                            (flatten 
                             (map #(subvec (apply vector %) 
                                           (max 0 (- x 1)) 
                                           (min (count (first board)) (+ x 2)))
                                  (subvec board (max 0 (- y 1)) (min (count board) (+ y 2)))))))]
  (map #(apply str %)
       (loop [r [] c (map #(let [s (if (= 9 (count %)) (= \# (nth % 4)))
                                 c (count (filter (fn [i] (= \# i)) %))]
                             (if (nil? s)
                               \ 
                               (if (and s (or (> c 4) (< c 3)))
                                 \ 
                                 (if (or s (= c 3))
                                   \#
                                   \ )))) (parse-neighbours))]
         (if (empty? c)
           r
           (recur (conj r (take (-> board first count) c)) (drop (-> board first count) c)))))))


;; Golf score: 339 (minified)
(fn g [b]
  (let [a apply
        i count
        j range
        v subvec
        n (-> b first i)
        m (i b)]
    (map #(a str %)
         (loop [r [] c (map #(let [s (if (= 9 (i %)) (= \# (nth % 4)))
                                   c (i (filter (fn [i] (= \# i)) %))]
                               (if (nil? s)
                                 \
                                 (if (and s (or (> c 4) (< c 3)))
                                   \
                                   (if (or s (= c 3))
                                     \#
                                     \ ))))
                            (for [y (j 0 m)
                                  x (j 0 n)]
                              (flatten
                               (map #(v (a vector %)
                                        (max 0 (- x 1))
                                        (min n (+ x 2)))
                                    (v b (max 0 (- y 1)) (min m (+ y 2)))))))]
           (if (= [] c)
             r
             (recur (conj r (take n c)) (drop n c)))))))