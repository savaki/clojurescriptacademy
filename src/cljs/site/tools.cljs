(ns site.tools
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [main.routes :as routes]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn template [{:keys [title timestamp body advanced-mode]}]
  [:html [:head [:meta {:charset "utf-8"}]
          [:meta {:name "viewport"
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href (str "/styles/website.css?" timestamp)
                  :rel "stylesheet"}]]
   [:body [:div#app [body]]
    (when (not advanced-mode)
      [:script {:type "text/javascript" :src "//fb.me/react-0.12.2.min.js"}])
    (when (not advanced-mode)
      [:script {:type "text/javascript" :src "/scripts/goog/base.js"}])
    [:script {:type "text/javascript" :src "/scripts/app.js"}]
    (when (not advanced-mode)
      [:script {:type "text/javascript"
                :dangerouslySetInnerHTML {:__html "goog.require('main.core');"}}])
    ]])

(defn ^:export render-page [path timestamp advanced-mode]
  (secretary/dispatch! path)
  (reagent/render-to-static-markup (template {:title "title"
                                              :timestamp timestamp
                                              :body routes/app-view
                                              :advanced-mode advanced-mode})))

