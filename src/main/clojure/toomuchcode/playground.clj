(ns toomuchcode.playground
  "Playground entrypoint"

  (:require [cemerick.austin.repls :refer (browser-connected-repl-js)]
            [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [clojure.java.browse :as browse]
            [ring.adapter.jetty :as ring]
            [hiccup.page :as page]))

(def content
  [:head
   [:link {:href  "css/bootstrap.min.css" :rel "stylesheet" :type "text/css"}]
   [:title "ClojureScript Playground"]
   [:body
    [:div {:id "details-modal"}]
    [:div {:class "container-fluid"}
     [:div {class "row"} [:h1 "Clojurescript Playground Hacks"]]
     [:div {:class "row"}
      [:div {:class "col-lg-2"}

       [:div {:class "panel panel-default"}
        [:div {:class "panel-heading"} "Experiments:"]
        [:ul {:class "list-group"}
         [:li {:class "list-group-item"} [:a {:href "#/om/modal"} "Om Modal"]]
         [:li {:class "list-group-item"} [:a {:href "#/om/multimodal"} "Om Multiroot Modal"]]
         [:li {:class "list-group-item"} [:a {:href "#/om/removelist"} "Items that can be removed"]]
         [:li {:class "list-group-item"} [:a {:href "#/reagent/modal"} "Reagent Modal"]]]]]

      [:div {:class  "col-lg-10 col-md-10 col-sm-10" }
       [:div {:id "app"}]]]]

    [:div
     [:script {:src "http://fb.me/react-0.11.1.js"}]
     [:script {:src "/js/react-bootstrap.min.js"}]
     [:script {:src "/js/playground.js"}]
     [:script (browser-connected-repl-js)]]]])

(defroutes routes
  (route/resources "/")
  (GET "/" [] (page/html5 content)))

;; Support for query parameters, session state, etc.
(def app (handler/site routes))

(defonce ^:private server (atom nil))

(defn start-server!
  "Starts a Jetty server to support the tools UI."
  []
  (when (nil? @server)
    (reset! server (ring/run-jetty #'app {:port 8080 :join? false}))))

(defn stop-server!
  []
  (when-let [jetty @server]
    (.stop jetty)
    (reset! server nil)))

(defn -main []
  (start-server!)

  (browse/browse-url (str "http://localhost:8080/"))

  (println "Press enter to exit...")
  (read-line)
  (stop-server!))
