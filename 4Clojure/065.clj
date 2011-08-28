;; See http://www.4clojure.com/problem/65

;; Clojure has many sequence types, which act in subtly different ways. The core
;; functions typically convert them into a uniform "sequence" type and work with
;; them that way, but it can be important to understand the behavioral and performance
;; differences so that you know which kind is appropriate for your application.

;; Write a function which takes a collection and returns one of :map, :set, :list,
;; or :vector - describing the type of collection it was given.

;; You won't be allowed to inspect their class or use the built-in predicates
;; like list? - the point is to poke at them and understand their behavior.

(fn [coll]
  (let [coll (conj coll [1 2])]
    (cond
     (= coll (conj coll coll)) :map
     (= coll (first (conj coll coll))) :list
     (= coll (conj coll (first coll))) :set
     :else :vector)))


;; Golf score: 76 (minified)
#(let [c conj
       f first
       i (c % [1 2])
       j (c i i)]
   (condp = i
     j :map
     (f j) :list
     (c i (f i)) :set
     :vector))