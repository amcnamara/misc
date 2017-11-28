;; See http://www.4clojure.com/problem/84

;; Write a function which generates the transitive closure of a binary relation.
;; The relation will be represented as a set of 2 item vectors.

;; NOTE: This solution doesn't check loops in the transitive graph in order to
;;       save space in the solution.  To catch this case, simply break the loop
;;       if val is already contained in the return collection.

(fn [coll]
  (letfn [(map-set [set] (reduce into (map #(apply hash-map %) set)))]
    (set
     (reduce into
             (for [node coll]
               (map #(vector (first node) %)
                    (loop [r [] val (first node)]
                      (let [next-node (get (map-set coll) val)]
                        (if next-node
                          (recur (conj r next-node) next-node)
                          r)))))))))


;; Golf score: 147 (minified)
(fn [c]
  (set
   (reduce into
           (for [i c]
             (map #(vector (first i) %)
                  (loop [r [] n (first i)]
                    (let [j (get (reduce into (map #(apply hash-map %) c)) n)]
                      (if j
                        (recur (conj r j) j)
                                        r))))))))