(ns nas-message.controllers.main
  (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.controller :as controller]
            [nas-message.api :as api]
            [antizer.reagent :as ant]
            [cljs.core.async :as async :refer [<! go-loop]]))

(defn update-counter-value [action app-db]
  (let [current (or (get-in app-db [:kv :counter]) 0)
        action-fn (if (= action :inc) inc dec)]
    (assoc-in app-db [:kv :counter] (action-fn current))))

(defn open-modal
  [app-db]
  (assoc-in app-db [:kv :modal-open?] true))

(defn close-modal
  [app-db]
  (assoc-in app-db [:kv :modal-open?] false))

(defn update-contract-state
  [app-db current-state]
  (when (and (get-in app-db [:kv :message-loaded?])
             (not= (:message current-state) (get-in app-db [:kv :message])))
    (ant/message-info "Message changed"))
  (-> app-db
      (assoc-in [:kv :message] (:message current-state))
      (assoc-in [:kv :paid-amount] (or (js/parseFloat (:paidAmount current-state)) 0))
      (assoc-in [:kv :amount-step] (js/parseFloat (:amountStep current-state)))
      (assoc-in [:kv :message-loaded?] true)))

(def pipelines
  {:update-contract-state (pipeline! [value app-db]
                                     (api/get-current-state)
                                     (pp/commit! (update-contract-state app-db value))
                                     (pp/rescue! [error]
                                                 (pp/commit!
                                                  (assoc-in app-db [:kv :message-loaded?] true))))
   :open-modal (pipeline! [value app-db]
                          (pp/commit! (open-modal app-db)))
   :close-modal (pipeline! [value app-db]
                           (pp/commit! (close-modal app-db)))
   :return-funds (pipeline! [value app-db]
                            (pp/commit! (assoc-in app-db [:kv :checking-funds?] true))
                            (api/return-funds {:simulate-call? true})
                            (if (= value "true")
                              (pipeline! [value app-db]
                                         (api/return-funds {:simulate-call? false})
                                         (ant/message-success "Your funds was returned."))
                              (ant/message-error "You don't have funds to return."))
                            (pp/commit! (assoc-in app-db [:kv :checking-funds?] false)))
   :set-message (pipeline! [value app-db]
                           (api/set-message value)
                           (ant/message-success
                            "Your message was submitted. It will appear here in a few seconds." 8)
                           (pp/rescue! [error]
                                       (ant/message-error "Your transaction was rejected." 8)))})

(defrecord Controller [])

(defmethod controller/params Controller [_ _] true)

(defmethod controller/start Controller [this _ app-db]
  (let [contract-state-updater (js/setInterval
                                #(controller/execute this :update-contract-state)
                                10000)]
    (controller/execute this :update-contract-state)
    (-> app-db
        (assoc-in [:kv :extension-missing?] (not (exists? js/webExtensionWallet)))
        (assoc-in [:kv :contract-state-updater] contract-state-updater)
        (assoc-in [:kv :modal-open?] false)
        (assoc-in [:kv :message-loaded?] false))))

(defmethod controller/stop Controller [this _ app-db]
  (do
    (js/clearInterval (get-in app-db [:kv :contract-state-updater]))
    app-db))

(defmethod controller/handler Controller [this app-db-atom in-chan _]
  (go-loop []
    (let [[command args] (<! in-chan)]
      (when-let [pipeline (get pipelines command)]
        (pipeline this app-db-atom args))
      (when command (recur)))))


