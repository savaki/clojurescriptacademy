(defproject CHANGE-ME-ME "0.1.0-SNAPSHOT"
  :description "CHANGE-ME"
  :url "https://CHANGE-ME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]
                 [reagent "0.5.0-alpha"]
                 [cljs-ajax "0.3.4"]]

  :plugins [[lein-environ "1.0.0"]
            [lein-cljsbuild "1.0.4"]]

  :preamble ["reagent/react.js"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :compiler {:optimizations :none
                                   :output-to "public/dev/app.js"
                                   :output-dir "public/dev/"
                                   :pretty-print true
                                   :source-map true}}
                       {:id "prod"
                        :source-paths ["src/cljs"]
                        :compiler {:optimizations :advanced
                                   :output-to "public/js/app.js"
                                   :output-dir "public/js/"
                                   :pretty-print true}}
                       ]}

  :min-lein-version "2.0.0")
