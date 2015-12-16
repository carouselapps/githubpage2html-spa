(ns githubpage2html-spa.app
  (:require [githubpage2html-spa.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
