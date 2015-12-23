(ns githubpage2html-spa.handlers
  (:require [re-frame.core :as re-frame]
            [githubpage2html-spa.db :as db]
            [ajax.core :as ajax]))

(re-frame/register-handler
  :initialize-db
  (fn [_ _]
    {:url-value    ""
     :pages        []
     :shown-pages #{}}))

(re-frame/register-handler
  :display-page-about
  (fn [db [_ _]]
    (assoc db :active-panel :about-panel)))

(re-frame/register-handler
  :display-page-pages
  (fn [db [_ _]]
    (when (not (contains? db :pages))
      (re-frame/dispatch [:get-pages]))
    (assoc db :active-panel :pages-panel)))

(re-frame/register-handler
  :got-pages
  (fn [db [_ pages]]
    (assoc db :pages pages)))

(re-frame/register-handler
  :get-pages
  (fn [db [_]]
    (ajax/GET "/api/v1/pages"
              {:handler       #(re-frame/dispatch [:got-pages %1])
               :error-handler (fn [{:keys [status status-text]}]
                                (js/alert "There was an unexpected error.")
                                (.log js/console (pr-str "Error" status status-text)))})
    db))

(re-frame/register-handler
  :show-hide-page
  (fn [db [_ id]]
    (let [new-shown-pages (if (some #{id} (:shown-pages db))
                             (disj (:shown-pages db) id)
                             (conj (:shown-pages db) id))]
      (assoc db :shown-pages new-shown-pages))))

(re-frame/register-handler
  :change-url-value
  (fn [db [_ value]]
    (assoc db :url-value value)))

(re-frame/register-handler
  :got-page
  (fn [db [_ page]]
    (assoc db :pages (into [page] (:pages db)))))

(re-frame/register-handler
  :get-url
  (fn [db _]
    (ajax/POST "/api/v1/page"
              {:params        {:url (:url-value db)}
               :handler       #(re-frame/dispatch [:got-page %1])
               :error-handler (fn [{:keys [status status-text]}]
                                (js/alert "There was an unexpected error.")
                                (.log js/console (pr-str "Error" status status-text)))})
    db))

(re-frame/register-handler
  :page-deleted
  (fn [db [_ id]]
    (assoc db :pages (remove #(= id (% "id")) (:pages db)))))

(re-frame/register-handler
  :delete-page
  (fn [db [_ id]]
    (ajax/POST "/api/v1/page/delete"
               {:params        {:id id}
                :handler       #(re-frame/dispatch [:page-deleted id])
                :error-handler (fn [{:keys [status status-text]}]
                                 (js/alert "There was an unexpected error.")
                                 (.log js/console (pr-str "Error" status status-text)))})
    db))