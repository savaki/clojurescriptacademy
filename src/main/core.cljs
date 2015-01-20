(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [partials.header :as header]
            [partials.footer :as footer]
            [pages.home :as home]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn app-view []
  [:div [header/page]
   [home/page]
   [footer/page]])

; guard access to js/document to allow offline rendering
(try
  (reagent/render-component [app-view] (.getElementById js/document "app"))
  (catch :default e e))


