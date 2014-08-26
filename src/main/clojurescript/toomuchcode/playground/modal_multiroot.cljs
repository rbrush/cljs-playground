(ns toomuchcode.playground.modal-multiroot
  "Experiment using a separate root for the modal.."
  (:require [om.core :as om]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [bootstrap-cljs :as bs :include-macros true]
            [secretary.core :as secretary :include-macros true :refer [defroute]]))

;; The person detail modal.
(def person-detail-state (atom {:person-details nil}))

(defcomponent person-details-modal [{:keys [person-details on-person-change] :as data} owner]
  (render [_]

          (dom/div

           ;; Render modal only when there are details set.
           (when (not (nil? person-details))
             (bs/modal {:title "Person info"
                        :animated false
                        :on-request-hide #(om/update! data :person-details nil)}

                       (dom/div (str (:name person-details) " lives in " (:city person-details)))

                       ;; Get a detached version of person-details, that we can access
                       ;; in the callback outside of an OM transaction to invoke the callback below.
                       ;; Is there a better way to handle this?
                       (let [detached (into {} person-details)]

                         (dom/div {:class "modal-footer"}

                                  (if (:vip person-details)
                                    (bs/button {:bsStyle "primary"
                                                :on-click (fn []
                                                            (on-person-change (assoc detached :vip false))
                                                            (om/update! data :person-details nil))}
                                               "Unmark as VIP")

                                    (bs/button {:bsStyle "primary"
                                                :on-click (fn []
                                                            (on-person-change (assoc detached :vip true))
                                                            (om/update! data :person-details nil))}
                                               "Mark as VIP"))

                                  (bs/button {:on-click #(om/update! data :person-details nil)}
                                             "Close"))))))))

(defn show-person-details-modal!
  "Show the person details modal. The on-person-change parameter is a callback
   that receives a new person structure if an update is made."
  [person-details on-person-change]
  (reset! person-detail-state  {:person-details person-details
                                :on-person-change on-person-change}))

;; Our playground application.
(def playground-state (atom {:people [{:name "Alice" :city "Kansas City" :vip false}
                                      {:name "Bob" :city "St. Louis" :vip false}
                                      {:name "Charles" :city "Chicago" :vip false}]}))

(defcomponent playground [{:keys [people] :as data} owner]
  (render [_]
          (dom/div "Hello, modal world with multiple roots!"
                   (dom/div
                    (bs/button-group
                     {:class "btn-group-vertical"}
                     (map-indexed (fn [index {:keys [name] :as person}]
                                    (bs/button
                                     {:bsStyle (if (:vip person) "danger" "default")
                                      :on-click #(show-person-details-modal!
                                                  person
                                                  (fn [updated] (om/update! data [:people index] updated )))} name))
                                  people))))))


;; Declare the route for our modal experiments.
(defroute "/multimodal" {:as params}

  (om/root playground playground-state
           {:target (.getElementById js/document "app")})

  (om/root person-details-modal person-detail-state
         {:target (.getElementById js/document "details-modal")}))
