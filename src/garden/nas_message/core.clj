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
          :background "linear-gradient(-45deg, #12c2e9, #c471ed, #f64f59)"
          :background-size "400% 400%"
          :animation [[gradient "15s" :ease :infinite]]}])

;; app styles

(defstyles app-styles
  [:#app {:display :grid
          :width "100vw"
          :height "100vh"}])

;; main styles

(defstyles main
  body-styles
  app-styles)
