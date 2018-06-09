(ns nas-message.api
  (:require
   vendor.nebpay
   [promesa.core :as p]))

(def nebpay (js/require "nebpay"))
(def nebpay-instance (nebpay.))
(def contract-address "n21fVsHG8BuubBggHPm7rk2wJTMekc9KJ5r")
(def default-options {:callback (.-testnetUrl (.-config nebpay))})

(defn get-current-state []
  (p/promise
   (fn [resolve reject]
     (if (exists? js/webExtensionWallet)
       (let [options (assoc default-options
                            :listener #(resolve (js->clj (.parse js/JSON (.-result %))
                                                         :keywordize-keys true)))]
         (.simulateCall nebpay-instance
                        contract-address
                        0
                        "getCurrentState"
                        nil
                        (clj->js options)))
       (reject (js/Error. "missing extension"))))))

(defn set-message
  [{:keys [message amount]}]
  (p/promise
   (fn [resolve reject]
     (let [options (assoc default-options
                          :listener #(if (= % "Error: Transaction rejected by user")
                                       (reject (js/Error. %))
                                       (resolve %)))]
       (.call nebpay-instance
              contract-address
              (js/parseFloat amount)
              "setMessage"
              (.stringify js/JSON (clj->js [message]))
              (clj->js options))))))

(defn return-funds
  [{:keys [simulate-call?]}]
  (p/promise
   (fn [resolve reject]
     (let [options (assoc default-options :listener #(resolve (.-result %)))]
       (if simulate-call?
         (.simulateCall nebpay-instance
                        contract-address
                        0
                        "returnFunds"
                        nil
                        (clj->js options))
         (.call nebpay-instance
                contract-address
                0
                "returnFunds"
                nil
                (clj->js options)))))))

