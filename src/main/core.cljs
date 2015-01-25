(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [pushy.core :as pushy :refer [push-state!]]
            [ajax.core :as ajax]

            [partials.header :as header]
            [partials.footer :as footer]
            [pages.home :as home]
            [pages.lesson :as lesson]))

(enable-console-print!)

(def app-state (atom {}))

(def current-page (atom home/page))

(defn app-view []
  [:div [header/page]
   [@current-page]
   [footer/page]])

; guard access to js/document to allow offline rendering
(try
  (reagent/render-component [app-view] (.getElementById js/document "app"))
  (catch :default e e))

;----------------------------------------------------------------------

(secretary/set-config! :prefix "/")
(push-state! secretary/dispatch!
  (fn [x] (when (secretary/locate-route x) x)))

(defroute "/" []
  (println "home page")
  (reset! current-page home/page))

(defroute "/lessons/:lesson-name" [lesson-name query-params]
  (println "lesson page - " lesson-name)
  (swap! app-state assoc :lesson-name lesson-name)
  (reset! current-page lesson/page))

