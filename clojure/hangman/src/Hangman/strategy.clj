(ns hangman.strategy
  (:require [clojure.string :as string]))

;; A collection of all of the valid words in the supplied dictionary
(def dictionary (distinct (map string/upper-case (string/split (slurp (str (System/getProperty "user.dir") "/resources/dict.txt")) #"\n"))))

(def ^:dynamic branch-words [])

;; Takes a word mask and returns a regex (for finding the subset of valid words for the given mask)
(defn- get-mask-regex
  ([mask]
    (get-mask-regex mask []))
  ([mask guesses]
    (re-pattern (string/replace mask "-" (if (empty? guesses) "[A-Z]" (str "[^" (apply str guesses) "]"))))))

;; Builds a word mask either of a given length or a collection of guessed chars against a given word
(defn- get-mask
  ([length]
    (apply str (repeat length "-")))
  ([guesses word]
    (apply str (map #(if (contains? (set guesses) (str %)) % "-") word)))
  ([pick mask word]
    (get-mask (conj (re-seq #"[A-Z]" mask) (str pick)) word)))

;; Filters a collection of words (default: branch-words) against a given mask
(defn- filter-mask-words
  ([mask guesses]
    (filter-mask-words mask guesses branch-words))
  ([mask guesses words]
    (filter #(re-find (get-mask-regex mask guesses) %) words)))

;; Grabs all valid words in the dictionary for a given mask
(defn get-words
  ([mask]
    (get-words mask []))
  ([mask guesses]
    (filter-mask-words mask guesses (get (group-by count dictionary) (count mask)))))

(defn get-masks [mask pick guesses]
  (distinct (map #(get-mask pick mask %) (get-words mask guesses))))

(defn- guess-branch [guesses rem-chars mask]
  (let [[pick _] (reduce (fn [[prev prev-n] test]
                           (let [test-n (count (re-seq (re-pattern (str test)) (apply str (filter-mask-words mask guesses branch-words))))]
                             (if (> test-n prev-n) [test test-n] [prev prev-n])))
                     [nil 0]
                     rem-chars)]
    {mask [pick
           (count (filter-mask-words mask guesses branch-words))
           (if pick
             (reduce into {}
               (map (fn [submask]
                      (binding [branch-words (filter-mask-words mask guesses branch-words)]
                        (guess-branch (conj guesses pick) (remove #{pick} rem-chars) submask)))
                    (distinct (map #(get-mask pick mask %) (filter-mask-words mask guesses branch-words)))))
             (first (filter-mask-words mask guesses branch-words)))]}))