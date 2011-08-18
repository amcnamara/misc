;; See http://www.4clojure.com/problem/91

;; Given a graph, determine whether the graph is connected. A connected graph is such that a path exists between any two given nodes.
;;  - Your function must return true if the graph is connected and false otherwise.
;;  - You will be given a set of tuples representing the edges of a graph. Each member of a tuple being a vertex/node in the graph.
;;  - Each edge is undirected (can be traversed either direction). 

;; Golf score: 296 (minified)

(fn [full-graph]
  (letfn [(connected [node graph]                                                                                                                                    
            (filter (complement nil?)                                                                                                                               
                    (map (fn [i] (if (= node (first i)) (second i)                                                                                                     
				     (if (= node (second i)) (first i))))                                                                                              
                         (seq graph))))                                                                                                                                 
         (nodes [graph]                                                                                                                                                         
            (distinct (flatten (seq graph))))]                                                                                                                               
    (loop [result [(-> full-graph first first)]]                                                                                                  
      (if (empty? (clojure.set/difference (set (nodes full-graph)) (set result)))                                                                                         
	true                                                                                                                                                  
	(if (empty? (clojure.set/difference (set (nodes (map (fn [i] (connected i full-graph)) result)))                                                                
					    (set result)))                                                                                                         
	  false               
	  (recur (into result (nodes (map (fn [i] (connected i full-graph)) result)))))))))