(ns zcoin-wallet.handlers.get-addr-transactions
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [reagent.core :as reagent :refer [atom]]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]]))


(def url "https://insight.zcoin.io/insight-api-zcoin/txs")

(defn fetch [addr]
  (prn :fetch-txs addr)
  (go (let [response (<! (http/get url
                                   {:with-credentials? false
                                    :query-params {:address addr}}))]
        (prn :status (:status response))
        (def data (:body response))
        (prn :body (:body response)))))


(defn get-unspent [addr txs]
  (let [flat-txs (for [tx txs
                       v (for [vout (get tx :vout)] vout)]
                   {:txid (get tx :txid)
                    :index (get v :n)
                    :value (get v :value)
                    :address (first (get-in v [:scriptPubKey :addresses]))
                    :spentTxId (get v :spentTxId)})]
                    ;:vout v})]
    (filter
      #(and (= addr (:address %))
            (nil? (:spentTxId %)))
      flat-txs)))

(comment

  (keys (:txs data))

  (first (:txs data))

  (get-in data [:txs 0 :vout])

  (for [x {:a 1 :b 2 :c 3}
        y [1]]
    [x y])

  (let [txs (:txs data)
        flat-txs (for [tx txs
                       v (for [vout (get tx :vout)] vout)]
                  {:txid (get tx :txid)
                   :index (get v :n)
                   :value (get v :value)
                   :address (first (get-in v [:scriptPubKey :addresses]))
                   :spentTxId (get v :spentTxId)})]
                   ;:vout v})]
     (filter
       #(and (= "a95vY5GUY7piVhe9iuPYYd2pysbxJMmJmY" (:address %))
             (nil? (:spentTxId %)))
       flat-txs))


         ;(into {})
         ;(mapcat count)))
         ;first))
         ;(map #(get-in % [1]))))

  (get-unspent "a95vY5GUY7piVhe9iuPYYd2pysbxJMmJmY" (:txs data))


  (fetch "a95vY5GUY7piVhe9iuPYYd2pysbxJMmJmY")
  (fetch "41qPsPR21M1TMpeZXaxuW5YhU6GeRSn2Rz")
  (fetch "aGLXJUXdd8HrovHcaqNSnXVZ9zQZ19ex2y"))




