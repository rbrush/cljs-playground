(ns toomuchcode.playground-client
  "Main entrypoint to the playground."
  (:require [goog.events :as events]
            [secretary.core :as secretary :include-macros true :refer [defroute]]
            [toomuchcode.playground.modal :as modal])
  (:import goog.History
           goog.history.EventType))

(let [h (History.)]
  (goog.events/listen h "navigate" #(secretary/dispatch! (.-token %)))
  (doto h
    (.setEnabled )))

;; Use our modal playground by default until others are added.
(secretary/dispatch! "/modal")
