(ns toomuchcode.playground.reagent.modal
  "Modal experiments with reagent."
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :include-macros true :refer [defroute]]))

(def person-detail-state (atom {:person-details nil}))

(defn modal
  "Returns a component representing a model."
  [{:keys [title content footer close-fn]}]
  [:div
   [:div.modal-backdrop.fade.in]
   [:div.modal.fade.in {:tabIndex "-1" :role "dialog" :style {:display "block"} :on-click close-fn}
    [:div.modal-dialog
     [:div.modal-content
      [:div.modal-header
       [:h4.modal-title title]]
      content
      [:div.modal-footer
       footer
       [:div.btn.btn-default {:type "button"
                              :on-click close-fn}
        "Close"]]]]]])

(defn person-details-modal []
 (let [{:keys [person-details on-person-change]} @person-detail-state
       close-fn #(swap! person-detail-state assoc :person-details nil)]

   ;; Render modal only when there are details set.
   (if (nil? person-details)
     [:div]
     (modal
      {:title "Person Info"
       :content [:div (str (:name person-details) " lives in " (:city person-details))]
       :footer (if (:vip person-details )
                 [:div.btn.btn-primary {:type "button"
                                        :on-click (fn []
                                                    (on-person-change (assoc person-details :vip false))
                                                    (close-fn))}
                  "Unmark as VIP"]
                 [:div.btn.btn-primary {:type "button"
                                        :on-click (fn []
                                                    (on-person-change (assoc person-details :vip true))
                                                    (close-fn))}
                  "Mark as VIP"])

       :close-fn close-fn}))))

(def playground-state (atom {:people [{:name "Alice" :city "Kansas City" :vip false}
                                      {:name "Robert" :city "St. Louis" :vip false}
                                      {:name "Charles" :city "Chicago" :vip false}]}))

(defn show-person-details-modal!
  [person-details on-person-change]
  (reset! person-detail-state  {:person-details person-details
                                :on-person-change on-person-change}))

(defn playground []
  [:div "Hello, reagent modal world!"
   [:div
    [:div.btn-group.btn-group-vertical
     (map-indexed
      (fn [index {:keys [name vip] :as person}]
        ^{:key index}
        [:button.btn
         {:class (if vip "btn-danger" "btn-default")
          :type "button"
          :on-click
          (fn [] (show-person-details-modal!
                 person
                 (fn [updated] (swap! playground-state assoc-in [:people index] updated ))))}
         name])
      (get @playground-state :people))]]])

(defroute "/reagent/modal" []
  (reagent/render-component [playground]
                            (.getElementById js/document "app"))

  (reagent/render-component [person-details-modal]
                            (.getElementById js/document "details-modal")))
