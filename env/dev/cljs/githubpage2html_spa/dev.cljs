(ns ^:figwheel-no-load githubpage2html-spa.app
  (:require [githubpage2html-spa.core :as core]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :on-jsload core/mount-root)

(core/init)
