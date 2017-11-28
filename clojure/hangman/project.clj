(defproject Hangman "0.0.1"
  :description "A predictive solver for Hangman puzzles."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [clj-config          "0.1.0"]]
  :main hangman.core
  :jvm-opts ["-Xmx2g" "-XX:+UseConcMarkSweepGC"]
  :disable-implicit-clean true)