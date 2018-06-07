(ns nas-message.api
  (:require
   vendor.nebpay
   [promesa.core :as p]))

(def nebpay (js/require "nebpay"))
(def nebpay-instance (nebpay.))
(def contract-address "n1uQNUCWU5KbQckvuvx43qsArPsLV23DDjB")
(def default-options {:callback (.-testnetUrl (.-config nebpay))})

(defn get-current-state []
  (p/promise
   (fn [resolve reject]
     (let [options (assoc default-options
                          :listener #(resolve (js->clj (.parse js/JSON (.-result %))
                                                       :keywordize-keys true)))]
       (.simulateCall nebpay-instance
                      contract-address
                      0
                      "getCurrentState"
                      nil
                      (clj->js options))))))

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

(defn return-funds [])

