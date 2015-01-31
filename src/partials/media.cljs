(ns partials.media
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(defn video-grid-item-view [video]
  [:a.video {:href "/lessons/first-thingie"} video])

(defn video-grid-view [videos]
  [:div.video-grid (for [video videos]
                     ^{:key video} [video-grid-item-view video])])
