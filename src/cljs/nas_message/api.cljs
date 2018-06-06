(ns nas-message.api
  (:require
   vendor.nebpay
   [promesa.core :as p]))

(def nebpay (js/require "nebpay"))
(def nebpay-instance (nebpay.))
(def contract-address "n1uQNUCWU5KbQckvuvx43qsArPsLV23DDjB")

(defn get-current-state []
  (p/promise
   (fn [resolve reject]
     (let [options {:callback (.-testnetUrl (.-config nebpay))
                    :listener #(resolve (js->clj (.parse js/JSON (.-result %))
                                                 :keywordize-keys true))}]
       (.simulateCall nebpay-instance
                      contract-address
                      0
                      "getCurrentState"
                      nil
                      (clj->js options))))))

(defn submit-message
  [message nas-amount])

(defn return-funds [])

