(ns zcoin.transaction
  (:require [oops.core :refer [oget oget+ oset! ocall oapply ocall! oapply!]]
            [zcoin.key :as k]
            [zcoin.mnemonic :as m]))

;(def Transaction (atom nil))
;(js/setTimeout (fn [] (reset! Transaction (aget js/window "deps" "zcoin-lib" "Transaction"))) 500)

(def zcoin-lib ((oget js/window "require") "bitcore-lib"))
(def Transaction (oget zcoin-lib "Transaction"))


(defn new-transaction
  ([]
   (if-not (nil? Transaction)
     (Transaction)))

  ([tx]
   (if-not (nil? Transaction)
     (Transaction tx))))

(defn add-input [tx {:keys [txid index address script amount]}]
  (if-not (nil? Transaction)
    (let [input {"txId" txid
                 "outputIndex" index
                 "address" address
                 "script" script
                 "satoshis" amount}]
      (ocall! tx :from (clj->js input)))))


(defn add-output [tx {:keys [address amount]}]
  (if-not (nil? Transaction)
      (ocall! tx :to address amount)))


(defn get-raw-tx [tx]
  (if-not (nil? Transaction)
    ;; TODO: Change unsafe=true to something else
    ;; TODO: Change unsafe=true to something else
    ;; TODO: Change unsafe=true to something else
    (ocall! tx :serialize true)))


(defn sign-tx [tx pv]
  (prn :sign-tx-with-pv pv)
  (ocall! tx :sign pv))

(defn is-fully-signed [tx]
  (ocall! tx :isFullySigned))




(comment


  "https://explorer.zcoin.io/api/getrawtransaction?txid=b933c0920f62d5ac5b4e8ea6a77507f4f265e320c33855a56315fc718016cffb&decrypt=1"
  (let [tx (new-transaction)
        tx1
        (add-input
          tx
          {:txid "b933c0920f62d5ac5b4e8ea6a77507f4f265e320c33855a56315fc718016cffb"
           :index 0
           :amount 500000
           :address "aGLXJUXdd8HrovHcaqNSnXVZ9zQZ19ex2y"
           :script "76a914ab5bd59ca1de058b5072cec2c1d4391f1b213bd188ac"})

        tx2
        (add-output
          tx1
          {:address "a95vY5GUY7piVhe9iuPYYd2pysbxJMmJmY"
           :amount 500000})

        hdpv (k/from-seed (m/to-seed "seed" "password"))

        pv (k/derive-key hdpv 0)
        pv-key (k/to-private-key pv)

        tx3 (sign-tx tx2 pv-key)]


    (prn :pv-key pv-key)

    (prn :tx3-is-fully-signed (ocall! tx3 :isFullySigned))
    (prn :tx3 (get-raw-tx tx3))))
