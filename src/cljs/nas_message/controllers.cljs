(ns nas-message.controllers
  (:require [nas-message.controllers.main :as c-main]))

(def controllers
  (-> {:main c-main/controller}))
