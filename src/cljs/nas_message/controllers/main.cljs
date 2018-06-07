(ns nas-message.controllers.main
  (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.pipeline.controller :as pp-controller]
            [nas-message.api :as api]
            [antizer.reagent :as ant]))

(defn update-counter-value [action app-db]
  (let [current (or (get-in app-db [:kv :counter]) 0)
        action-fn (if (= action :inc) inc dec)]
    (assoc-in app-db [:kv :counter] (action-fn current))))

(defn start
  [app-db]
  (-> app-db
      (assoc-in [:kv :modal-open?] false)
      (assoc-in [:kv :message-loaded?] false)))

(defn open-modal
  [app-db]
  (assoc-in app-db [:kv :modal-open?] true))

(defn close-modal
  [app-db]
  (assoc-in app-db [:kv :modal-open?] false))

(defn update-contract-state
  [app-db current-state]
  (-> app-db
      (assoc-in [:kv :message] (:message current-state))
      (assoc-in [:kv :paid-amount] (:paidAmount current-state))
      (assoc-in [:kv :message-loaded?] true)))

(def controller
  (pp-controller/constructor
   (fn [_] true)
   {:start (pipeline! [value app-db]
                      (pp/commit! (start app-db))
                      (api/get-current-state)
                      (pp/commit! (update-contract-state app-db value)))
    :open-modal (pipeline! [value app-db]
                           (pp/commit! (open-modal app-db)))
    :close-modal (pipeline! [value app-db]
                            (pp/commit! (close-modal app-db)))
    :set-message (pipeline! [value app-db]
                            (api/set-message value)
                            (ant/message-success
                             "Your message was submitted. It will appear here in a few seconds." 8)
                            (pp/rescue! [error]
                                        (ant/message-error "Your transaction was rejected." 8)))}))


