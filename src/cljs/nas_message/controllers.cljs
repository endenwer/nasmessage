(ns nas-message.controllers
  (:require [nas-message.controllers.counter :as counter]))

(def controllers
  (-> {:counter counter/controller}))
