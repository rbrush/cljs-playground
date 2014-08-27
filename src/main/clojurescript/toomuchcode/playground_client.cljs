(ns toomuchcode.playground-client
  "Main entrypoint to the playground."
  (:require [goog.events :as events]
            [secretary.core :as secretary :include-macros true :refer [defroute]]
            [toomuchcode.playground.om.modal]
            [toomuchcode.playground.om.modal-multiroot]
            [toomuchcode.playground.reagent.modal]
            [toomuchcode.playground.om.removelist])
  (:import goog.History
           goog.history.EventType))

(let [h (History.)]
  (goog.events/listen h "navigate" #(secretary/dispatch! (.-token %)))
  (doto h
    (.setEnabled true)))
