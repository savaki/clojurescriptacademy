(ns partials.header
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn organization-view []
  [:div.organization [:h1 "ClojureScript Academy"]])

(defn navigation-view []
  [:ul.navigation])

(defn search-view []
  [:div.search])

(defn page []
  [:div.header [:div.content [organization-view]
                [navigation-view]
                [search-view]]])

