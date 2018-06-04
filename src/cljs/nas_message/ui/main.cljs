(ns nas-message.ui.main
  (:require
   vendor.nebpay
   [keechma.ui-component :as ui]
   [keechma.toolbox.ui :refer [sub> <cmd]]
   [antizer.reagent :as ant]
   [reagent.core :as r]))

(def nebpay (js/require "nebpay"))
(.log js/console (nebpay.))

(defn message-render
  [message]
  [:div.message message])

(defn new-message-button-render
  [ctx]
  [ant/button {:class "new-message-btn"
               :ghost true
               :size "large"
               :on-click #(<cmd ctx :open-modal)} "Place your message"])

(defn description-section-render []
  [:div.description-section
   [:h2 "How it works"]
   [:p "Aliquam erat volutpat.  Nunc eleifend leo vitae magna.  In id erat non orci commodo lobortis.  Proin neque massa, cursus ut, gravida ut, lobortis eget, lacus.  Sed diam.  Praesent fermentum tempor tellus.  Nullam tempus.  Mauris ac felis vel velit tristique imperdiet.  Donec at pede.  Etiam vel neque nec dui dignissim bibendum.  Vivamus id enim.  Phasellus neque orci, porta a, aliquet quis, semper a, massa.  Phasellus purus.  Pellentesque tristique imperdiet tortor.  Nam euismod tellus id erat."]])

(defn modal-render
  [ctx]
  (r/with-let [new-message (r/atom nil)]
    [:div.modal
     [:div.close-btn
      [ant/icon {:type :close :on-click #(<cmd ctx :close-modal)}]]
     [description-section-render]
     [:div.form-section
      [:label.message-input-label (str "Your message (" (count @new-message) " of 140)")]
      [ant/input-text-area {:class "message-input"
                            :on-change #(reset! new-message (.-value (.-target %)))
                            :value @new-message
                            :placeholder "Input your message"}]
      [ant/button {:class "cancel-btn" :size "large"} "Cancel"]
      [ant/button {:class "submit-btn" :size "large" :tyle "primary"} "Submit"]]
     [:div.modal-footer
      [:span "footer"]]]))

(defn render [ctx]
  [:div.app-container
   [message-render (sub> ctx :message)]
   [new-message-button-render ctx]
   (when (sub> ctx :modal-open?) [modal-render ctx])])

(def component (ui/constructor {:renderer render
                                :topic :main
                                :subscription-deps [:message :modal-open?]}))
