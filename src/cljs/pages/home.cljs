(ns pages.home
  (:require [reagent.core :as reagent :refer [atom]]
            [analytics :as analytics]
            [partials.media :as media]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn hero-video-item [{:keys [label video path]}]
  [:td.hero-video [:a {:href "/lessons/do-something-with-something" :on-click #(println "render video")} [:span.label label]]])

(defn hero-videos-view []
  [:table.hero-videos [:tr [hero-video-item "thirty second video"]
                       [hero-video-item "two minute tutorial"]
                       [hero-video-item "Getting started"]]])

(defn hero-view []
  [:div.hero [:div.content [:h1 "Learn ClojureScript and Reagent. Now."]
              [:p "Five minutes can revolutionize how you do web development."]
              [hero-videos-view]]])

(defn page []
  [:div [hero-view]
   [:div.choose-a-series [:div.content [media/video-grid-view ["Video #1"]]
                          [:h1 "Moar Videos Coming Soon"]]]])

