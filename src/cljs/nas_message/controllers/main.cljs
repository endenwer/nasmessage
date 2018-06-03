(ns nas-message.controllers.main
  (:require [keechma.toolbox.pipeline.core :as pp :refer-macros [pipeline!]]
            [keechma.toolbox.pipeline.controller :as pp-controller]))

(defn update-counter-value [action app-db]
  (let [current (or (get-in app-db [:kv :counter]) 0)
        action-fn (if (= action :inc) inc dec)]
    (assoc-in app-db [:kv :counter] (action-fn current))))

(defn start
  [app-db]
  (-> app-db
      (assoc-in [:kv :modal-open?] false)
      (assoc-in [:kv :message]  "Curabitur lacinia pulvinar nibh. Donec vitae dolor. Integer placerat tristique nisl. Integer placerata tristique nisl. Integer placera nisl.")))

(defn open-modal
  [app-db]
  (assoc-in app-db [:kv :modal-open?] true))

(defn close-modal
  [app-db]
  (assoc-in app-db [:kv :modal-open?] false))

(def controller
  (pp-controller/constructor
   (fn [_] true)
   {:start (pipeline! [value app-db]
                      (pp/commit! (start app-db)))
    :open-modal (pipeline! [value app-db]
                           (pp/commit! (open-modal app-db)))
    :close-modal (pipeline! [value app-db]
                            (pp/commit! (close-modal app-db)))}))

