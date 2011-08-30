;; See http://www.4clojure.com/problem/89

;; Starting with a graph you must write a function that returns true if it is possible to
;; make a tour of the graph in which every edge is visited exactly once.

;; The graph is represented by a vector of tuples, where each tuple represents a single edge.

;; The rules are:
;; - You can start at any node.
;; - You must visit each edge exactly once.
;; - All edges are undirected.

;; NOTE: This solution works by factoring node references and reducing the graph by extending
;; an edge via a common node reference, subgraphs which extend more than two nodes (i) are
;; exhausted before dual and self-referential nodes (j) in order to prevent subgraph edges
;; from becoming unreachable.  The function x reduces two edges to produce an edge between the
;; non-common nodes being reduced (it is convoluted in order to correctly parse dual and self-
;; referential nodes)... though it could use some refactoring for clarity and maintainability.

;; Golf score: 291
(fn f [c]
  (if (= 1 (count c))
    true
    (let [h (first c)
          g (rest c)
          i (first (filter #(and (some (set h) %) (not= h (reverse %)) (not= h %)) g))
          j (first (filter #(some (set h) %) g))
          x (fn [i] (concat (vector (take 2 (flatten (vals (sort (group-by count (partition-by identity (sort (concat i h)))))))))
                            (remove #{i} g)))]
      (if i
        (f (x i))
        (if j
          (f (x j))
          false)))))