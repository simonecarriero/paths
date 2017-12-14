(defproject paths "1.0.0"
  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.2.0"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.1"]
                 [ring/ring-json "0.4.0"]
                 [com.cognitect/transit-clj "0.8.300"]
                 [com.cognitect/transit-cljs "0.8.243"]
                 [keybind "2.2.0"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :cljsbuild {:builds
              [{:source-paths ["src/cljs"]
                :compiler     {:output-to     "resources/public/js/paths.js"
                               :optimizations :whitespace
                               :pretty-print  true}}]}

  :clean-targets ^{:protect false} [:target-path "resources/public/js/"]

  :main paths.core)
