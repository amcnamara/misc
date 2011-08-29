;; See http://www.4clojure.com/problem/85

;; Write a function which generates the power set of a given set. The power set of a set x
;; is the set of all subsets of x, including the empty set and x itself.

;; NOTE: Since this might be tricky to parse later, the logic goes that I create a sequence
;; of binary masks of 0 to (count c) in big endian format, and mask those bits to parse out
;; elements from the input set, the full binary seq maps to every element of the power set.

;; Golf score: 144
(fn [c]
  (set
   (if (empty? c)
     #{#{}}
     (map (fn [m]
            (set (reduce into
                         (map #(if (= \1 %) #{%2}) m c))))
                     (map #(reverse (Integer/toString % 2)) (range (Math/pow 2 (count c))))))))