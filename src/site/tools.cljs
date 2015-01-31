(ns site.tools
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [main.core :as core]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn template [{:keys [title timestamp body]}]
  [:html [:head [:meta {:charset "utf-8"}]
          [:meta {:name "viewport"
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href (str "/styles/website.css?" timestamp)
                  :rel "stylesheet"}]]
   [:body [:div#app [body]]
    [:script {:type "text/javascript"
              :src "/scripts/app.js"}]]])

(defn ^:export render-page [path timestamp]
  (secretary/dispatch! path)
  ;  (reagent/render-component-to-string (template {:title "title"
  (reagent/render-to-static-markup (template {:title "title"
                                              :timestamp timestamp
                                              :body core/app-view})))

