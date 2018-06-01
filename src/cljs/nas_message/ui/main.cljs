(ns nas-message.ui.main
  (:require
   vendor.nebpay
   [keechma.ui-component :as ui]
   [keechma.toolbox.ui :refer [sub> <cmd]]))

(def nebpay (js/require "nebpay"))
(.log js/console (nebpay.))

(defn render [ctx]
  [:div
   [:button {:on-click #(<cmd ctx :update :dec)} "Decrement"]
   [:button {:on-click #(<cmd ctx :update :inc)} "Increment"]
   [:p (str "Count: " (sub> ctx :counter))]])

(def component (ui/constructor {:renderer render
                                :topic :counter
                                :subscription-deps [:counter]}))
