(ns nas-message.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [nas-message.core-test]))

(doo-tests 'nas-message.core-test)
