(ns githubpage2html-spa.config
  (:require [taoensso.timbre :as timbre]))

(def defaults
  {:init
   (fn []
     (timbre/info "\n-=[githubpage2html-spa started successfully]=-"))
   :middleware identity})
