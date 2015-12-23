(ns githubpage2html-spa.handler
  (:require [compojure.core :refer [defroutes routes wrap-routes ANY context]]
            [githubpage2html-spa.layout :refer [error-page]]
            [githubpage2html-spa.middleware :as middleware]
            [githubpage2html-spa.routes.services :refer [service-routes]]
            [githubpage2html-spa.db.core :as db]
            [githubpage2html-spa.layout :as layout]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.3rd-party.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [githubpage2html-spa.config :refer [defaults]]
            [mount.core :as mount]))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  (timbre/merge-config!
    {:level     ((fnil keyword :info) (env :log-level))
     :appenders {:rotor (rotor/rotor-appender
                          {:path (or (env :log-path) "githubpage2html_spa.log")
                           :max-size (* 512 1024)
                           :backlog 10})}})
  (doseq [component (:started (mount/start))]
    (timbre/info component "started"))
  ((:init defaults)))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "githubpage2html-spa is shutting down...")
  (doseq [component (:stopped (mount/stop))]
    (timbre/info component "stopped"))
  (timbre/info "shutdown complete!"))

(defroutes
  app-routes
  (var service-routes)
  (context "/api" [] (route/not-found "Not Found"))
  (wrap-routes (routes (ANY "*" [] (layout/render "app.html")))
           middleware/wrap-csrf)
  (route/not-found "Not Found"))

(def app (middleware/wrap-app #'app-routes))
