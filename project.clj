(defproject nas-message "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.8.1"]
                 [keechma "0.3.6" :exclusions [reagent
                                               cljsjs/react-with-addons
                                               cljsjs/react-dom
                                               cljsjs/react-dom-server]]
                 [keechma/toolbox "0.1.13" :exclusions [funcool/promesa
                                                        reagent
                                                        cljsjs/react-with-addons
                                                        cljsjs/react-dom
                                                        cljsjs/react-dom-server]]
                 [funcool/promesa "1.8.1"]
                 [garden "1.3.5"]
                 [antizer "0.2.2"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-garden "0.3.0"]]

  :clean-targets ^{:protect false} ["resources/public/js"
                                    "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :garden {:builds
           [{:id "screen"
             :source-paths ["src/garden"]
             :stylesheet nas-message.core/main
             :compiler {:output-to "resources/public/css/main.css"
                        :vendors ["moz" "webkit"]
                        :auto-prefix #{:keyframes :animation}}}]}

  :profiles
  {:dev
   {:dependencies [
                   [figwheel-sidecar "0.5.17-SNAPSHOT"]
                   [com.cemerick/piggieback "0.2.1"]
                   [binaryage/devtools "0.8.2"]]

    :plugins      [[lein-figwheel "0.5.17-SNAPSHOT"]
                   [lein-doo "0.1.7"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "nas-message.core/reload"}
     :compiler     {:main                 nas-message.core
                    :externs ["externs.js"]
                    :foreign-libs         [{:file "vendor_js/nebPay.js"
                                            :file-min "vendor_js/nebPay.min.js"
                                            :provides ["vendor.nebpay"]}]
                    :optimizations        :none
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/dev"
                    :asset-path           "js/dev"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config
                    {:devtools/config
                     {:features-to-install    [:formatters :hints]
                      :fn-symbol              "F"
                      :print-config-overrides true}}}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            nas-message.core
                    :externs ["externs.js"]
                    :foreign-libs    [{:file "vendor_js/nebPay.js"
                                       :file-min "vendor_js/nebPay.min.js"
                                       :provides ["vendor.nebpay"]}]
                    :optimizations   :advanced
                    :output-to       "resources/public/js/app.js"
                    :output-dir      "resources/public/js/min"
                    :elide-asserts   true
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:output-to     "resources/public/js/test.js"
                    :foreign-libs  [{:file "vendor_js/nebPay.js"
                                     :file-min "vendor_js/nebPay.min.js"
                                     :provides ["vendor.nebpay"]}]
                    :output-dir    "resources/public/js/test"
                    :main          nas-message.runner
                    :optimizations :none}}]})
