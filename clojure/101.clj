;; See: http://www.4clojure.com/problem/101

;; Given two sequences x and y, calculate the Levenshtein distance of x and y, i. e. the
;; minimum number of edits needed to transform x into y. The allowed edits are:

;; - insert a single item
;; - delete a single item
;; - replace a single item with another item

;; WARNING: Some of the test cases may timeout if you write an inefficient solution!

(defn f [s1 s2]
  (loop [r 0 i s1 j s2]
    (let [a (first i)
          b (first j)
          nexta (first (rest i))
          nextb (first (rest j))
          counti (count i)
          countj (count j)]
      (if (empty? i)
        r
        (if (= a b)
          (recur r (rest i) (rest j))
          (if (or (= nexta nextb) (= counti countj))
            (recur (inc r) (rest i) (rest j))
            (if (> counti countj)
              (recur (inc r) (rest i) j)
              (recur (inc r) i (rest j)))))))))


;; Golf score: 173 (minimized).
#(loop [r 0 i % j %2]
   (let [x first
         y count
         z rest
         c (y i)
         d (y j)
         e (z i)
         f (z j)
         n (inc r)]
     (if (empty? i)
       (+ r d)
       (if (= (x i) (x j))
         (recur r e f)
         (if (or (= (x e) (x f)) (= c d))
           (recur n e f)
           (if (> c d)
             (recur n e j)
             (recur n i f)))))))