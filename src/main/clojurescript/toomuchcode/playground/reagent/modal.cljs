(ns toomuchcode.playground.reagent.modal
  "Modal experiments with reagent."
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :include-macros true :refer [defroute]]))

(def person-detail-state (atom {:person-details nil}))

(defn person-details-modal []
  (let [{:keys [person-details on-person-change]} @person-detail-state]

    [:div

     ;; Render modal only when there are details set.
     (when (not (nil? person-details))
       (js/ReactBootstrap.Modal
        #js {:title "Person info"
             :animated false
             :onRequestHide #(swap! person-detail-state assoc :person-details nil)}

        (reagent/as-component [:div (str (:name person-details) " lives in " (:city person-details))])

        (reagent/as-component
         [:div.modal-footer

          (if (:vip person-details)
            (js/ReactBootstrap.Button
             #js {:bsStyle "primary"
                  :onClick (fn []
                             (on-person-change (assoc person-details :vip false))
                             (swap! person-detail-state assoc :person-details nil))}

             "Unmark as VIP")

            (js/ReactBootstrap.Button
             #js {:bsStyle "primary"
                  :onClick (fn []
                             (on-person-change (assoc person-details :vip true))
                             (swap! person-detail-state assoc :person-details nil))}

             "Mark as VIP"))

          (js/ReactBootstrap.Button #js {:onClick #(swap! person-detail-state assoc :person-details nil)}
                                    "Close")])))]))

(def playground-state (atom {:people [{:name "Alice" :city "Kansas City" :vip false}
                                      {:name "Bob" :city "St. Louis" :vip false}
                                      {:name "Charles" :city "Chicago" :vip false}]}))

(defn show-person-details-modal!
  [person-details on-person-change]
  (reset! person-detail-state  {:person-details person-details
                                :on-person-change on-person-change}))

(defn playground []
  [:div "Hello, reagent modal world!"
   [:div
    (apply js/ReactBootstrap.ButtonGroup
           #js {:className "btn-group-vertical"}
           (map-indexed
            (fn [index {:keys [name] :as person}]
              (js/ReactBootstrap.Button #js
                                        {:bsStyle (if (:vip person) "danger" "default")
                                         :onClick #(show-person-details-modal!
                                                    person
                                                    (fn [updated] (swap! playground-state assoc-in [:people index] updated )))}
                                        name))

            (get  @playground-state :people)))]])

(defroute "/reagent/modal" []
  (reagent/render-component [playground]
                            (.getElementById js/document "app"))

  (reagent/render-component [person-details-modal]
                            (.getElementById js/document "details-modal")))
