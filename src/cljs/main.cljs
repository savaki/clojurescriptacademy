(ns main.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(defn app-view []
	[:div.sample "Hello There"])

(reagent/render-component [app-view] (.getElementById js/document "app"))


