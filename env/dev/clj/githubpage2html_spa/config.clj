(ns githubpage2html-spa.config
  (:require [selmer.parser :as parser]
            [taoensso.timbre :as timbre]
            [githubpage2html-spa.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (timbre/info "\n-=[githubpage2html-spa started successfully using the development profile]=-"))
   :middleware wrap-dev})
