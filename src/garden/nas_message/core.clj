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
          :animation [[gradient "15s" :ease :infinite]]}])

;; app styles
(defstyles message-styles
  [:.message {:font-size "3em"
              :font-weight "800"
              :text-shadow "1px 1px 1px rgba(0,0,0,0.3)"
              :width "66%"
              :text-align :center}])

(defstyles new-message-btn-styles
  [:.new-message-btn {:color :white}])

(defstyles app-styles
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
