(ns partials.media
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(defn video-grid-item-view [video]
  [:div video])

(defn video-grid-view [videos]
  [:div
   (for [video videos] [video-grid-item-view video])])
