(ns nas-message.ui.main
  (:require
   vendor.nebpay
   [keechma.ui-component :as ui]
   [keechma.toolbox.ui :refer [sub> <cmd]]))

(def nebpay (js/require "nebpay"))
(.log js/console (nebpay.))

(defn message-render
  [message]
  [:div.message message])

(defn new-message-button-render
  [ctx]
  [:button.new-message-btn {:on-click #(<cmd ctx :open-modal)} "Place your message"])

(defn modal-render
  [ctx]
  [:div "Hello"])

(defn render [ctx]
  [:<>
   [:div.app-container
    [message-render (sub> ctx :message)]
    [new-message-button-render ctx]]
   (when (sub> ctx :modal-open?) [modal-render])])

(def component (ui/constructor {:renderer render
                                :topic :main
                                :subscription-deps [:message :modal-open?]}))
