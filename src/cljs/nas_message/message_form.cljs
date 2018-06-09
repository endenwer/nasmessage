(ns nas-message.message-form
  (:require [reagent.core :as reagent]
            [forms.core :as f]
            [forms.validator :as v]))

(def not-blank [:not-blank (fn [v] (not (empty? (str v))))])
(def not-zero [:not-zero (fn [v] (not= v 0))])
(def less-than-140-symbols [:less-then-140-symbols (fn [v] (<= (count v) 140))])

(def validator
  (v/validator {:new-message [less-than-140-symbols not-blank]
                :amount [not-zero not-blank]}))

(defn set-path
  [path value data-atom]
  (swap! data-atom assoc-in path value))

(def form (f/constructor validator))



