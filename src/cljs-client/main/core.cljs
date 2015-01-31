(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [pushy.core :as pushy :refer [push-state!]]

            [main.routes :as routes]
            [partials.footer :as footer]
            [pages.home :as home]
            [pages.lesson :as lesson]))

(reagent/render-component [routes/app-view] (.getElementById js/document "app"))

(secretary/set-config! :prefix "/")

(push-state! secretary/dispatch!
  (fn [x] (when (secretary/locate-route x) x)))

