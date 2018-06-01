(ns nas-message.core
  (:require [reagent.core :as reagent]
            [keechma.app-state :as app-state]
            [keechma.toolbox.dataloader.app :as dataloader]
            [keechma.toolbox.forms.app :as forms]
            [nas-message.controllers :refer [controllers]]
            [nas-message.ui :refer [ui]]
            [nas-message.subscriptions :refer [subscriptions]]
            [nas-message.edb :refer [edb-schema]]
            [nas-message.datasources :refer [datasources]]
            [nas-message.forms :as nas-message-forms]))

(def app-definition
  (-> {:components    ui
       :controllers   controllers
       :subscriptions subscriptions
       :html-element  (.getElementById js/document "app")}
      (dataloader/install datasources edb-schema)
      (forms/install nas-message-forms/forms nas-message-forms/forms-automount-fns)))

(defonce running-app (clojure.core/atom nil))

(defn start-app! []
  (reset! running-app (app-state/start! app-definition)))

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (let [current @running-app]
    (if current
      (app-state/stop! current start-app!)
      (start-app!))))

(defn ^:export main []
  (dev-setup)
  (start-app!))
