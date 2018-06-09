(ns nas-message.core
  (:require [garden.def :refer [defstyles defkeyframes]]))

;; ant styles

(defstyles ant-styles
  [:.ant-spin-dot [:i {:background-color "#fff"}]])

;; body styles

(defkeyframes gradient
  ["0%" {:background-position "0% 50%"}]
  ["50%" {:background-position "100% 50%"}]
  ["100%" {:background-position "0% 50%"}])

(defstyles body-styles
  gradient
  [:body {:padding 0
          :margin 0
          :font-family "'Roboto', sans-serif"
          :background "linear-gradient(-45deg, #12c2e9, #c471ed, #f64f59)"
          :background-size "400% 400%"
          :font-size "17px"
          :animation [[gradient "15s" :ease :infinite]]}])

;; app styles

(defstyles form-styles
  [:.has-error {:border-color "#f5222d"}
   [:&:focus {:border-color "#f5222d"
              :box-shadow "0 0 0 2px rgba(245,34,45,.2)"}]
   ["&:not([disabled]):hover" {:border-color "#f5222d"}]])

(defstyles modal-styles
  form-styles
  [:.modal {:position :absolute
            :display :grid
            :grid-gap "15px 0"
            :grid-template-columns "1fr 2fr 2fr 1fr"
            :grid-template-rows "1fr auto 1fr"
            :grid-template-areas (str "'. . . close-btn'"
                                      "'. description form .'"
                                      "'. footer footer .'")
            :width "100%"
            :height "100%"
            :background-color "#fff"
            :color "#545454"}
   [:.close-btn {:grid-area "close-btn"
                 :display :grid
                 :height "50px"
                 :justify-content :end
                 :font-size "3em"
                 :margin "15px 22px"
                 :color "#949494"}
    [:i {:cursor "pointer"}
     [:&:hover {:color "#545454"}]]]
   [:.description-section {:grid-area "description"
                           :padding "15px"
                           :border-right "1px solid #e8e8e8"}
    [:h2 {:margin-top 0}]]
   [:.form-section {:grid-area "form"
                    :padding "15px"
                    :display :grid
                    :grid-gap "10px"
                    :grid-template-rows "20px auto auto auto"
                    :grid-template-columns "1fr"
                    :grid-template-areas (str "'message-input-label'"
                                              "'message-input'"
                                              "'amount-input'"
                                              "'submit'"
                                              "'return-funds'")}
    [:.return-funds-btn {:grid-area "return-funds"}]
    [:.amount-input {:grid-area "amount-input"
                     :height "40px"
                     :font-size "17px !important"}]
    [:.ant-input-group-addon {:font-size "17px"}]
    [:button {:height "50px"
              :font-size "17px"}]
    [:.message-input-label {:grid-area "message-input-label"
                            :font-size "17px"}]
    [:.message-input {:grid-area "message-input"
                      :font-size "17px"}]
    [:.cancel-btn {:grid-area "cancel"}]
    [:.submit-btn {:grid-area "submit"}]
    [:.extension-missing {:color :red
                          :text-align :center}]]
   [:.modal-footer {:grid-area :footer
                    :display :grid
                    :justify-content :center
                    :align-items :end
                    :color "#b5b5b5"
                    :margin-bottom "10px"}]])

(defstyles message-styles
  [:.message {:display :grid
              :width "66%"
              :justify-items :center}
   [:.message-body {:font-size "2em"
                    :color "#fff"
                    :font-weight "800"
                    :text-shadow "1px 1px 1px rgba(0,0,0,0.3)"
                    :text-align :center}]
   [:.message-paid-amount {:color "rgba(255,255,255,0.5)"
                           :font-weight :bold
                           :text-align :center}]])

(defstyles new-message-btn-styles
  [:.new-message-btn {:margin-bottom "50px"
                      :border "2px solid rgba(255, 255, 255, 0.4)"
                      :color "rgba(255, 255, 255, 0.4)"}
   [:&:active {:border-color "rgba(255, 255, 255, 0.8)"
               :color "rgba(255, 255, 255, 0.8)"}]
   [:&:hover :&:focus {:border-color "#fff"
                       :color "#fff"}]])

(defstyles app-styles
  modal-styles
  message-styles
  new-message-btn-styles
  [:#app {:display :grid
          :align-items :center
          :justify-items :center
          :width  "100vw"
          :height "100vh"}]
  [:.app-container {:display :grid
                    :grid-template-rows "1fr auto";
                    :align-items :center
                    :justify-items :center
                    :width "100vw"
                    :height "100vh"}])

;; main styles

(defstyles main
  ant-styles
  body-styles
  app-styles)
