;; See: http://www.4clojure.com/problem/53

;; Given a vector of integers, find the longest consecutive sub-sequence of increasing numbers. If two sub-sequences have
;; the same length, use the one that occurs first. An increasing sub-sequence must have a length of 2 or greater to qualify.

#(loop [r [] c % i []]
   (if (empty? c)
     r
     (let [j (conj i (first c))]
       (if (and (> (count j) (count r)) (> (count j) 1))
         (if (= (inc (first c)) (first (rest c)))
           (recur j (rest c) j)
           (recur j (rest c) []))
         (if (= (inc (first c)) (first (rest c)))
           (recur r (rest c) j)
           (recur r (rest c) []))))))

;; Golf score: 195 (minified)
#(loop [r [] c % i []]
   (if (empty? c)
     r
     (let [f first
           n count
           s rest
           j (conj i (f c))]
       (if (and (> (n j) (n r)) (> (n j) 1))
         (if (= (inc (f c)) (f (s c)))
           (recur j (s c) j)
           (recur j (s c) []))
         (if (= (inc (f c)) (f (s c)))
           (recur r (s c) j)
           (recur r (s c) []))))))