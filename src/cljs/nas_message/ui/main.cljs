(ns nas-message.ui.main
  (:require
   [keechma.ui-component :as ui]
   [keechma.toolbox.ui :refer [sub> <cmd]]
   [antizer.reagent :as ant]
   [reagent.core :as r]
   [nas-message.message-form :as message-form]
   [forms.core :as f]))

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
  (r/with-let [form (message-form/form {:amount "" :new-message ""})]
    (let [form-data-atom (f/data form)
          form-data @form-data-atom
          new-message (:new-message form-data)
          amount (:amount form-data)
          on-change-handler (fn [path]
                              (fn [e]
                                (message-form/set-path  path (.-value (.-target e)) form-data-atom)
                                (when-not @(f/is-valid-path? form path) (f/validate! form true))))]
      (.log js/console @(f/is-valid-path? form [:new-message]))
      [:div.modal
       [:div.close-btn
        [ant/icon {:type :close :on-click #(<cmd ctx :close-modal)}]]
       [description-section-render]
       [:div.form-section
        [:label.message-input-label (str "Your message (" (count new-message) " of 140)")]
        [ant/input-text-area {:class (str "message-input "
                                          (when-not @(f/is-valid-path? form [:new-message])
                                            "has-error"))
                              :autosize {:max-rows 4 :min-rows 4}
                              :on-blur #(f/validate! form true)
                              :on-change (on-change-handler [:new-message])
                              :value new-message
                              :placeholder "Input your message"}]
        [ant/input {:class (str "amount-input "
                                (when-not @(f/is-valid-path? form [:amount]) "has-error"))
                    :value amount
                    :on-blur #(f/validate! form true)
                    :on-change (on-change-handler [:amount])
                    :addon-before "NAS amount (min 0.0001 NAS)"}]
        [ant/button {:class "submit-btn"
                     :size "large"
                     :type "primary"
                     :on-click #(f/validate! form)} "Submit"]
        [ant/button {:class "retunr-funds-btn" :size "large"} "Return funds"]]
       [:div.modal-footer
        [:span "footer"]]])))

(defn render [ctx]
  [:div.app-container
   [message-render (sub> ctx :message)]
   [new-message-button-render ctx]
   (when (sub> ctx :modal-open?) [modal-render ctx])])

(def component (ui/constructor {:renderer render
                                :topic :main
                                :subscription-deps [:message :modal-open?]}))
