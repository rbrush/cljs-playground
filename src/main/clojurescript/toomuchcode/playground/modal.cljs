(ns toomuchcode.playground.modal
  "Playground for modals."
  (:require [om.core :as om]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [bootstrap-cljs :as bs :include-macros true]
            [secretary.core :as secretary :include-macros true :refer [defroute]]))

(def playground-state (atom {:people [{:name "Alice" :city "Kansas City"}
                                      {:name "Bob" :city "St. Louis"}
                                      {:name "Charles" :city "Chicago"}]

                             ;; The person details modal is displayed when this is not nil.
                             :person-details nil}))

(defcomponent person-details-modal [{:keys [person-details] :as data} owner]
  (render [_]

          (dom/div

           ;; Render modal only when there are details set.
           (when (not (nil? person-details))
             (bs/modal {:title "Person info"
                        :animated false
                        :on-request-hide #(om/update! data :person-details nil)}

                       (dom/div (str (:name person-details) " lives in " (:city person-details)))

                       (dom/div {:class "modal-footer"}
                                (bs/button {:on-click #(om/update! data :person-details nil)}
                                           "Close")))))))

(defcomponent playground [{:keys [people] :as data} owner]
  (render [_]
          (dom/div "Hello, modal world!"
                   (dom/div
                    (bs/button-group {:class "btn-group-vertical"}
                     (for [{:keys [name] :as person} people]

                       (bs/button {:on-click #(om/update! data :person-details person)} name))))

                   ;; Include the person details modal, which determines whether it should be displayed.
                   (->person-details-modal data))))

;; Declare the route for our modal experiments.
(defroute "/modal" {:as params}

  (om/root playground playground-state
         {:target (.getElementById js/document "app")}))
