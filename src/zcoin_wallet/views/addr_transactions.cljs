(ns zcoin-wallet.views.addr-transactions
  (:require [zcoin-wallet.handlers.get-addr-transactions :as h]))


(defn field [label value]
  [:div.row
   [:div.col-md-2 label]
   [:div.col-md-6 value]])

;; TODO: Use schema to validate data
;; TODO: show source address
(defn transaction [{:keys [txid value]}]
  [:div.row
    [field "TxID" txid]
    [field "Value" value]])


(defn page []
  (let [txs (h/get-transactions "a95vY5GUY7piVhe9iuPYYd2pysbxJMmJmY")]
    [:div
     [:h2 "Transactions"]
     [:div
      (for [tx txs]
        [transaction tx])]]))

(comment
  (h/get-transactions "a95vY5GUY7piVhe9iuPYYd2pysbxJMmJmY")

  (h/get-transactions ""))
