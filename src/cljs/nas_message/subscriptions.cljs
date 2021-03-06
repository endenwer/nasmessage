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
   :extension-missing? (get-kv :extension-missing?)
   :paid-amount (get-kv :paid-amount)
   :modal-open? (get-kv :modal-open?)
   :amount-step (get-kv :amount-step)
   :checking-funds? (get-kv :checking-funds?)
   :message-loaded? (get-kv :message-loaded?)})
