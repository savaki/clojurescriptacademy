(ns pages.home
  (:require [reagent.core :as reagent :refer [atom]]
            [analytics :as analytics]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn page[]
  [:div "hello world 123"])

