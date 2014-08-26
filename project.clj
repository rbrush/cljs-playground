(defproject org.toomuchcode/cljs-playground "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojurescript "0.0-2202"]
                 [ring/ring-jetty-adapter "1.2.1"]
                 [compojure "1.1.6"]
                 [cljs-ajax "0.2.6"]
                 [hiccup "1.0.4"]
                 [bootstrap-cljs "0.0.2"]
                 [om "0.7.1"]
                 [prismatic/om-tools "0.3.2"]
                 [secretary "1.2.0"]
                 [reagent "0.4.2"]
                 [prismatic/schema "0.2.4"]]

  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :java-source-paths ["src/main/java"]
  :plugins [[lein-cljsbuild "1.0.3"]]
  :hooks [leiningen.cljsbuild]

  :main toomuchcode.playground
  :profiles {:dev {:plugins [[com.cemerick/austin "0.1.4"]]}}

  :cljsbuild {:builds
              [{:source-paths ["src/main/clojurescript"]
                :compiler {
                           :output-to "resources/public/js/playground.js"
                           :optimizations :whitespace
                           :pretty-print true}}]}

  :scm {:name "git"
        :url "https://github.com/rbrush/cljs-playground.git"}

  :pom-addition [:developers [:developer
                              [:id "rbrush"]
                              [:name "Ryan Brush"]
                              [:url "http://www.toomuchcode.org"]]]

  :deploy-repositories [["snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                                      :creds :gpg}]])
