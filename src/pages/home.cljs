(ns pages.home
  (:require [reagent.core :as reagent :refer [atom]]
            [analytics :as analytics]
            [partials.media :as media]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn hero-video-item [video]
  [:td.hero-video [:a [:span.label video]]])

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
   [:div.choose-a-series [:div.content [:h1 "Moar Videos Coming Soon"]
                          [media/video-grid-view []]]]])

