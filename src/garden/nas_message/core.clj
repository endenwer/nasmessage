(ns nas-message.core
  (:require [garden.def :refer [defstyles defkeyframes]]))

;; body styles

(defkeyframes gradient
  ["0%" {:background-position "0% 50%"}]
  ["50%" {:background-position "100% 50%"}]
  ["100%" {:background-position "0% 50%"}])

(defstyles body-styles
  gradient
  [:body {:padding 0
          :margin 0
          :color "#fff"
          :font-family "'Roboto', sans-serif"
          :background "linear-gradient(-45deg, #12c2e9, #c471ed, #f64f59)"
          :background-size "400% 400%"
          :font-size "17px"
          :animation [[gradient "15s" :ease :infinite]]}])

;; app styles

(defstyles modal-styles
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
                    :grid-template-rows "20px auto auto"
                    :grid-template-columns "1fr 1fr"
                    :grid-template-areas (str "'message-input-label message-input-label'"
                                              "'message-input message-input'"
                                              "'cancel submit'")}
    [:button {:height "50px"
              :font-size "17px"}]
    [:.message-input-label {:grid-area "message-input-label"
                            :font-size "17px"}]
    [:.message-input {:grid-area "message-input"
                      :border-radius "5px"
                      :border "2px solid #e8ebed"
                      :transition ".2s"
                      :padding "12px 15px"
                      :font-size "1.2em"
                      :outline 0}
     [:&:focus {:border "2px solid #d4d9dd"}]]
    [:.cancel-btn {:grid-area "cancel"}]
    [:.submit-btn {:grid-area "submit"
                   :color "#fff"
                   :background "#5fcf80"
                   :border "2px solid #5fcf80"}]]
   [:.modal-footer {:grid-area :footer
                    :display :grid
                    :justify-content :center
                    :align-items :end
                    :color "#b5b5b5"
                    :margin-bottom "10px"}]])

(defstyles message-styles
  [:.message {:font-size "3em"
              :font-weight "800"
              :text-shadow "1px 1px 1px rgba(0,0,0,0.3)"
              :width "66%"
              :text-align :center}])

(defstyles new-message-btn-styles
  [:.new-message-btn {:margin-bottom "50px"}])

(defstyles app-styles
  modal-styles
  message-styles
  new-message-btn-styles
  [:.app-container {:display :grid
                    :grid-template-rows "1fr auto";
                    :align-items :center
                    :justify-items :center
                    :width "100vw"
                    :height "100vh"}])

;; main styles

(defstyles main
  body-styles
  app-styles)
