(ns main.routes
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :refer-macros [defroute]]

            [partials.header :as header]
            [partials.footer :as footer]
            [pages.home :as home]
            [pages.lesson :as lesson]))

(enable-console-print!)

(def app-state (atom {}))

(def current-page (atom home/page))

(defn app-view []
  [:div 
   [header/page]
   [@current-page]
   [footer/page]])

;----------------------------------------------------------------------

(secretary/set-config! :prefix "/")

(defroute "/" []
  (println "home page")
  (reset! current-page home/page))

(defroute "/lessons/:lesson-name" [lesson-name query-params]
  (println "lesson page - " lesson-name)
  (swap! app-state assoc :lesson-name lesson-name)
  (reset! current-page lesson/page))

