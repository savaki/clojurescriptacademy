(ns analytics
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :as ajax]))

(defn track [event properties]
  (.track js/analytics event properties))

(defn identify [id properties]
  (.identify js/analytics id properties))

