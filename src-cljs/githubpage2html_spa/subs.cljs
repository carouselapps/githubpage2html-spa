(ns githubpage2html-spa.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
  :active-panel
  (fn [db _]
    (reaction (:active-panel @db))))

(re-frame/register-sub
  :pages
  (fn [db _]
    (reaction (:pages @db))))

(re-frame/register-sub
  :shown-pages
  (fn [db _]
    (reaction (:shown-pages @db))))

(re-frame/register-sub
  :url-value
  (fn [db _]
    (reaction (:url-value @db))))