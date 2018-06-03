(ns nas-message.subscriptions
  (:require [keechma.toolbox.dataloader.subscriptions :as dataloader]
            [nas-message.edb :refer [edb-schema]]
            [nas-message.datasources  :refer [datasources]])
  (:require-macros [reagent.ratom :refer [reaction]]))

(defn get-kv [key]
  (fn [app-db-atom]
    (reaction
     (get-in @app-db-atom (flatten [:kv key])))))

(def subscriptions
  {:message (get-kv :message)
   :modal-open? (get-kv :modal-open?)})
