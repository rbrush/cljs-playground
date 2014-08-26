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
    [:div {:id "app"}
     [:ul
      [:li [:a {:href "#/om/modal"} "Om Modal"]]
      [:li [:a {:href "#/om/multimodal"} "Om Multiroot Modal"]]
      [:li [:a {:href "#/reagent/modal"} "Reagent Modal"]]]]
    [:div {:id "details-modal"}]
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
