;; See http://www.4clojure.com/problem/91

;; Given a graph, determine whether the graph is connected. A connected graph is such that a path exists between any two given nodes.
;;  - Your function must return true if the graph is connected and false otherwise.
;;  - You will be given a set of tuples representing the edges of a graph. Each member of a tuple being a vertex/node in the graph.
;;  - Each edge is undirected (can be traversed either direction). 

(fn [full-graph]
  (let [connected #(filter (complement nil?)                                                                                                                      
                           (map (fn [[i j]]
                                  (if (= % i) j
                                  (if (= % j) i)))
                           (seq graph)))
        nodes     #(flatten (seq %))]                                                                                                                            
    (loop [result [(first (nodes full-graph))]]                                                        
      (if (= (set (nodes full-graph)) (set result))
        true
        (if (= (set (nodes (map (fn [i] (connected i full-graph)) result))) (set result))
          false
          (recur (into result (nodes (map (fn [i] (connected i full-graph)) result)))))))))


;; Golf score: 208 (minified)
(fn [a]
  (let [s set
        m map
        c #(filter (complement nil?)
                   (m (fn [[i j]] (if (= % i) j
                                      (if (= % j) i)))
                      (seq %2)))
        n #(flatten (seq %))]
    (loop [r [(first (n a))]]
      (if (= (s (n a)) (s r))
        true
        (if (= (s (n (m #(c % a) r))) (s r))
          false
          (recur (into r (n (m #(c % a) r)))))))))