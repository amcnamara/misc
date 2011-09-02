;; See: http://www.4clojure.com/problem/92

;; Roman numerals are easy to recognize, but not everyone knows all the rules necessary to work with
;; them. Write a function to parse a Roman-numeral string and return the number; it represents.

;; You can assume that the input will be well-formed, in upper-case, and follow the subtractive principle.
;; You don't need to handle any numbers greater than MMMCMXCIX (3999), the largest number representable
;; with ordinary letters.

;; Golf score: 216
(fn [n]
  (let [f first
        d count
        v [["M" 1000]
           ["CM" 900]
           ["D"  500]
           ["C"  100]
           ["XC"  90]
           ["XL"  40]
           ["X"   10]
           ["IX"   9]
           ["V"    5]
           ["IV"   4]
           ["I"    1]]]
    (loop [r 0 c n]
      (if (empty? c)
        r
        (let [[k v] (f (filter #(if (= (seq (f %)) (take (d (f %)) c)) %) v))]
                    (recur (+ r v) (drop (d k) c)))))))