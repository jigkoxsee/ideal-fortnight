(ns ui.core
  (:require [reagent.core :as r]))

(defn two-col-form [label body]
  [:div.form-group.row
   [:label.col-sm-2.col-form-label label]
   [:div.col-sm-10 body]])


(defn inline-form [label & body]
  [:div.form-inline
   [:label.col-sm-2.col-form-label label]
   body])
