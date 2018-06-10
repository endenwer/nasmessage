(ns nas-message.ui.main
  (:require
   [keechma.ui-component :as ui]
   [keechma.toolbox.ui :refer [sub> <cmd]]
   [antizer.reagent :as ant]
   [reagent.core :as r]
   [nas-message.message-form :as message-form]
   [forms.core :as f]
   [clojure.string :as string]))

(defn message-render
  [message paid-amount extension-missing?]
  (if extension-missing?
    [:div.message
     [:div.message-body
      [:span "WebExtensionWallet is not installed, please install it first "]
      [:br]
      [:a {:href "https://github.com/ChengOrangeJu/WebExtensionWallet"} "Install Extension"]]]
    (if (nil? message)
      [:div.message
       [:div.message-body "You can place your message here"]]
      [:div.message
       [:div.message-body message]
       [:div.message-paid-amount (str "Rented for " paid-amount " NAS")]])))

(defn new-message-button-render
  [ctx]
  [ant/button {:class "new-message-btn"
               :ghost true
               :size "large"
               :on-click #(<cmd ctx :open-modal)} "Place your message"])

(defn description-section-render
  [amount-step]
  [:div.description-section
   [:h2 "How it works"]
   [:p (str "You can place a message by paying NAS. "
            "Your money is stored in a smart contract and you can "
            "return it any time and your message will be deleted. ")]
   [:p (str "If someone pays more than you (at least " amount-step " NAS more), "
            "the message will be changed, and you will receive your NAS back.")]])

(defn modal-render
  [ctx]
  (r/with-let [form (message-form/form {:amount 0 :new-message ""})]
    (let [form-data-atom (f/data form)
          form-data @form-data-atom
          new-message (:new-message form-data)
          paid-amount (sub> ctx :paid-amount)
          amount-step (sub> ctx :amount-step)
          min-amount (.toFixed (+ paid-amount amount-step) 5)
          amount (:amount form-data)
          on-change-handler (fn [path]
                              (fn [e]
                                (message-form/set-path  path (.-value (.-target e)) form-data-atom)
                                (when-not @(f/is-valid-path? form path) (f/validate! form true))))]
      [:div.modal
       [:div.close-btn
        [ant/icon {:type :close :on-click #(<cmd ctx :close-modal)}]]
       [description-section-render (sub> ctx :amount-step)]
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
                    :on-blur (fn [e]
                               (let [value (js/parseFloat
                                            (string/replace (.-value (.-target e)) #"," "."))]
                                 (message-form/set-path
                                  [:amount]
                                  (if (or (< value min-amount) (js/isNaN value)) min-amount value)
                                  form-data-atom))
                               (f/validate! form true))
                    :on-change (on-change-handler [:amount])
                    :addon-before (str "NAS amount (min " (js/parseFloat min-amount) " NAS)")}]
        [ant/button {:class "submit-btn"
                     :disabled (sub> ctx :extension-missing?)
                     :size "large"
                     :type "primary"
                     :on-click (fn []
                                 (f/validate! form)
                                 (when @(f/is-valid? form)
                                   (<cmd ctx :set-message {:message new-message :amount amount})))}
         "Submit"]
        [ant/button {:class "return-funds-btn"
                     :disabled (sub> ctx :extension-missing?)
                     :size "large"
                     :loading (sub> ctx :checking-funds?)
                     :on-click #(<cmd ctx :return-funds)}
         "Return funds"]
        (when (sub> ctx :extension-missing?)
          [:div.extension-missing
           [:span "WebExtensionWallet is not installed, please install it first "]
           [:a {:href "https://github.com/ChengOrangeJu/WebExtensionWallet"} "Install Extension"]])]
       [:div.modal-footer
        [:span "© NasMessage"]
        [:a {:href "https://github.com/endenwer/nasmessage"} "Source code"]
        [:a {:href "#"} "Smart contract"]]])))

(defn render [ctx]
  [:div.app-container
   (if (sub> ctx :message-loaded?)
     [:<>
      [message-render (sub> ctx :message) (sub> ctx :paid-amount) (sub> ctx :extension-missing?)]
      [new-message-button-render ctx]]
     [ant/spin {:size :large}])
   (when (sub> ctx :modal-open?) [modal-render ctx])])

(def component (ui/constructor {:renderer render
                                :topic :main
                                :subscription-deps [:message
                                                    :extension-missing?
                                                    :message-loaded?
                                                    :amount-step
                                                    :checking-funds?
                                                    :modal-open?
                                                    :paid-amount]}))
