(ns nas-message.ui.main
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> <cmd]]))

(defn render [ctx]
  [:div
   [:button {:on-click #(<cmd ctx :update :dec)} "Decrement"]
   [:button {:on-click #(<cmd ctx :update :inc)} "Increment"]
   [:p (str "Count: " (sub> ctx :counter))]])

(def component (ui/constructor {:renderer render
                                :topic :counter
                                :subscription-deps [:counter]}))
