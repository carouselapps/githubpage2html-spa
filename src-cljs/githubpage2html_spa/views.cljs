(ns githubpage2html-spa.views
  (:require
    [re-frame.core :as re-frame]
    [githubpage2html-spa.routes :as routes]
    [githubpage2html-spa.ui :as ui]
    [cljs-time.core :as t]
    [cljs-time.format :as f]
    [cljs-time.coerce :as time]
    [reagent.core :as reagent]))


;; pages

(defn input-component []
  (let [url-value (re-frame/subscribe [:url-value])]
    [:div.row
     [:div.span12
      [:form {:on-submit  #(ui/dispatch % [:get-url])}
       [:p
        [:span "URL:"]
        [:input {:on-change #(re-frame/dispatch [:change-url-value (-> % .-target .-value)]) :class "form-control" :type "text" :name "url" :defaultValue @url-value :placeholder "https://www.github.com/..."}]]
       [:input {:type "submit" :class "btn btn-primary" :value "get" :disabled (empty? @url-value)}]]]]))

(defn page-component [page show-it]
  [:li (f/unparse (f/formatter "yyyy-MM-dd HH:mm") (time/from-date (js/Date. (page "timestamp"))))
   [:p (page "url") " " [:a {:style {:cursor "pointer"} :on-click #(re-frame/dispatch [:delete-page (page "id")])} "[x]"]]
   [:textarea {:style {:margin-bottom "15px"} :class "form-control" :rows "4" :cols "50" :name "content" :value (page "content") :readOnly true}]
   [:button {:style {:margin-bottom "15px"} :type "button" :class "btn btn-info" :on-click #(re-frame/dispatch [:show-hide-page (page "id")])} (if show-it "hide" "show")]
   (when show-it [:div {:style {:border-style "solid" :border-color "#5bc0de" :padding "10px"} :dangerouslySetInnerHTML {:__html (page "content")}}])
   ])

(defn pages-panel []
  (let [pages (re-frame/subscribe [:pages])
        shown-pages (re-frame/subscribe [:shown-pages])]
    (fn [] [:div.container
            [input-component]
            [:div.row
             [:div.span12
             [:ul (doall (for [page @pages]
                          ^{:key (page "id")} [page-component page (some #{(page "id")} @shown-pages)]))]]]])))

;; about

(defn about-panel []
  (fn []
    [:div
     [:p "Extract main article text from github pages to be used in Wordpress."]
     [:p "More information at: " [:a {:href "https://github.com/carouselapps/githubpage2html"} "https://github.com/carouselapps/githubpage2html"]]]))


;; main

(defmulti panels identity)
(defmethod panels :pages-panel [] [pages-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:div
       [:nav.navbar.navbar-inverse.navbar-fixed-top
        [:div.container
         [:div.navbar-header
          [:button.navbar-toggle.collapsed {:type "button", :data-toggle "collapse", :data-target "#navbar", :aria-expanded "false", :aria-controls "navbar"}
           [:span.sr-only "Toggle navigation"]
           [:span.icon-bar]
           [:span.icon-bar]
           [:span.icon-bar]]
          [:a.navbar-brand {:href "/"} "githubpage2html-spa"]]
         [:div#navbar.collapse.navbar-collapse
          [:ul.nav.navbar-nav
           [:li                                             ;{:class "active"}
            [:a {:href "/"} "Home"]]
           [:li                                             ;{:class (when (= :about (:name @current-route)) "active")}
            [:a {:href (routes/url-for :about)} "About"]]]]]]
       [:main.container
        (panels @active-panel)]])))