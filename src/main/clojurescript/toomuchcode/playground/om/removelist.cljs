(ns toomuchcode.playground.om.removelist
  "Allow removal of items from the list."
  (:require [om.core :as om]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as dom :include-macros true]
            [bootstrap-cljs :as bs :include-macros true]
            [secretary.core :as secretary :include-macros true :refer [defroute]]))

(def initial-people [{:name "Alice" :city "Kansas City" :vip false}
                     {:name "Bob" :city "St. Louis" :vip false}
                     {:name "Charles" :city "Chicago" :vip false}])

;; Our playground application.
(def playground-state (atom {:people initial-people}))

(comment
  (defcomponent playground [{:keys [people] :as data} owner]
    (render [_]
            (dom/div
             (bs/panel
              "Hello, modal world with multiple roots!"
              (dom/div {:class "row"}
                       (dom/div {:class "col-lg-2 col-md-2 col-sm-2"}

                                (bs/button-group
                                 {:class "btn-group-vertical"}

                                 (map (fn [{:keys [name] :as person}]
                                        (bs/button
                                         {:bsStyle (if (:vip person) "danger" "default")
                                          :on-click #(om/update! data :people (remove (fn [x] (= person x)) people ))}
                                         (dom/span {:class "glyphicon glyphicon-remove pull-left"
                                                    :style {:margin-right "20px"}})
                                         name))

                                      people))))

              (dom/div {:class "row"}
                       (dom/div {:class "col-lg-2 col-md-2 col-sm-2"}
                                (bs/button-group {:class "btn-group-vertical"}
                                                 (bs/button {:bsStyle "danger"
                                                             :on-click #(om/update! data :people initial-people)}
                                                            "Reset!")))))

             ))))

(defcomponent removelist [{:keys [people] :as data} owner]
  (render [_]
          (dom/div
           {:class "container-fluid"}
           (dom/div
            {:class "col-lg-2"}

            (dom/div {:class "row"}
                     (dom/div {:class "panel panel-default"}
                              (dom/div {:class "panel-heading"} "Here are the people.")
                              (dom/ul
                               {:class "list-group"}
                               (for [{:keys [name] :as person} people]
                                 (dom/li {:class "list-group-item"}
                                         name
                                         (dom/span {:class "glyphicon glyphicon-remove pull-right"
                                                    :style {:margin-right "20px"}
                                                    :onClick  #(om/update! data :people (remove (fn [x] (= person x)) people ))}))
                                 ))))

            (dom/div {:class "row"}
                     (bs/button {:bsStyle "danger"
                                 :on-click  #(om/update! data :people initial-people)}
                                "Reset!"))

            ))))

;; Declare the route for our modal experiments.
(defroute "/om/removelist" {:as params}

  (om/root removelist playground-state
           {:target (.getElementById js/document "app")}))
