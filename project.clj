(defproject website "0.1.0-SNAPSHOT"
  :description "clojurescript academy website"
  :url "https://clojurescriptacademy.com"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]
                 [cljs-ajax "0.3.4"]
                 [com.facebook/react "0.12.2.1"]
                 [kibu/pushy "0.2.2"]
                 [reagent "0.5.0-alpha"]
                 [secretary "1.2.1"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs" "src/cljs-client"]
                        :compiler {:optimizations :none
                                   :preamble ["react/react.min.js"]
                                   :output-to "target/dev/scripts/app.js"
                                   :output-dir "target/dev/scripts"
                                   :source-map "target/dev/scripts/app.js.map"
                                   :pretty-print true}}
                       {:id "prod"
                        :source-paths ["src/cljs" "src/cljs-client"]
                        :compiler {:optimizations :advanced
                                   :preamble ["react/react.min.js"]
                                   :output-to "target/public/scripts/app.js"
                                   :output-dir "target/public/scripts"
                                   :source-map "target/public/scripts/app.js.map"
                                   :pretty-print false}}
                       {:id "server"
                        :source-paths ["src/cljs"]
                        :compiler {:optimizations :none
                                   :output-to "target/server/app.js"
                                   :output-dir "target/server"
                                   :pretty-print true}}
                       ]}

  :min-lein-version "2.0.0")

