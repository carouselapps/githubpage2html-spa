(ns githubpage2html-spa.config)

(def debug?
  ^boolean js/goog.DEBUG)

(when debug?
  (enable-console-print!))