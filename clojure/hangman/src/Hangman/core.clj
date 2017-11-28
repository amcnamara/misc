(ns hangman.core
  (:require [hangman.strategy    :as strategy]
            [clojure.string      :as string])
  (:import  [com.factual.hangman HangmanGame HangmanGame$Status]))

(def max-guesses 8)
(def weight      1)

(defn -main [& args]
  (if-let [secret-word-list (try (string/split (slurp (first args)) #"\s+") (catch Exception _ (if (first args) args)))]
    (map #(let [game (HangmanGame. % max-guesses)]
            (loop [guessed-chars [] guessed-words [] mask (.getGuessedSoFar game)]
              (println game)
              (when (= HangmanGame$Status/KEEP_GUESSING (.gameStatus game))
                (let [branches (map
                                 (fn [test]
                                   [test (strategy/get-masks mask test guessed-chars)])
                                 (remove (set guessed-chars) (map char (range 65 91))))
                      words    (remove (set guessed-words) (strategy/get-words mask guessed-chars))
                      total    (count words)
                      dmax     (apply max (map (fn [[_ masks]] (count masks)) branches))
                      [answer] words
                      [pick _] (reduce
                                 (fn [[min-pick min-score] [test masks]]
                                   (let [dtest (count masks)
                                         score (+ (/ (apply +
                                                       (map (fn [submask] (* (/ total dtest) (count (strategy/get-words submask `(~@guessed-chars ~test))))) masks))
                                                     dtest)
                                                  (* weight (+ (- dmax dtest) 1)))]
                                     (if (or (nil? min-score) (< score min-score))
                                       [test score]
                                       [min-pick min-score])))
                                 [nil nil]
                                 branches)]
                  (if (or (nil? pick) (>= 2 (count words)))
                    (do
                      (println "Guessing word:" answer)
                      (.. game (guessWord answer))
                      (recur guessed-chars (conj guessed-words answer) mask))
                    (do
                      (println "Guessing character:" pick)
                      (.. game (guessLetter pick))
                      (recur (conj guessed-chars pick) guessed-words (.getGuessedSoFar game))))))))
          secret-word-list)
    (println "Usage: lein run <words...|word-filename>, where the word file is whitespace delimited and there exists a dictionary file under ./resources/dict.txt")))