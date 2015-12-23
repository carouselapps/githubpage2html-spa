(ns githubpage2html-spa.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [githubpage2html-spa.db.core :as db]
            [githubpage2html.core :as g2html]))

(defn get-page [url]
  (let [record {:url       url
               :timestamp (java.util.Date.)
               :content   (g2html/gen-html (g2html/get-content url))}]
    ((if (db/get-page url) db/update-page! db/save-page!)
      record)
    (db/get-page url)))

(defapi service-routes
        (ring.swagger.ui/swagger-ui "/api")
        (swagger-docs {:info {:title "githubpage2html api"}})
        (context* "/api/v1" []
                  :tags ["v1"]
                  (GET* "/pages" []
                        :summary "Return all the pages."
                        (ok (db/get-pages)))
                  (POST* "/page" []
                         :summary "Get a page from github."
                         :body-params [url :- String]
                         (ok (get-page url)))
                  (POST* "/page/delete" []
                         :summary "Delete a page."
                         :body-params [id :- Long]
                         (ok (db/delete-page! id)))))