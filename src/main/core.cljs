(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [pages.home :as home]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn app-view []
  [home/page])

; guard access to js/document to allow offline rendering
(try
  (reagent/render-component [app-view] (.getElementById js/document "app"))
  (catch :default e e))


