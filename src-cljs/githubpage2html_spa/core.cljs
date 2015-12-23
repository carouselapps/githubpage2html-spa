(ns githubpage2html-spa.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [githubpage2html-spa.handlers]
              [githubpage2html-spa.subs]
              [githubpage2html-spa.routes :as routes]
              [githubpage2html-spa.views :as views]
              [githubpage2html-spa.config :as config]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/start!)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))