(ns partials.footer
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(defn page []
  [:div.footer [:div.content [:p.copyright "©2015 ClojureScript Academy, All Rights Reserved."]]])

