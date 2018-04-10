(ns zcoin-wallet.views.sign-tx
  (:require
    [reagent.core :as reagent :refer [atom]]
    [oops.core :refer [oget oget+ oset! ocall oapply ocall! oapply!]]

    [zcoin.key :as k]
    [zcoin.transaction :as t]
    [zcoin.mnemonic :as m]))


(defonce input-atom
         (atom
           {:txid ""
            :index 0
            :amount 0
            :address ""
            :script ""}))

(defonce output-atom
         (atom
           {:address ""
            :amount 0}))

(defonce signature-atom
         (atom {}))

(defonce result-atom
         (atom {}))


(defn two-col-form [label body]
  [:div.form-group.row
   [:label.col-sm-2.col-form-label label]
   [:div.col-sm-10 body]])

(defn my-swap! [a k v]
  (swap! a assoc k v))

(defn input-swap!
  ([k]
   (fn [e]
     (let [val (oget e :target :value)]
       (my-swap! input-atom k val))))
  ([k convert]
   (fn [e]
     (let [val (oget e :target :value)]
       (my-swap! input-atom k (convert val))))))

(defn output-swap!
  ([k]
   (fn [e]
     (let [val (oget e :target :value)]
       (my-swap! output-atom k val))))
  ([k convert]
   (fn [e]
     (let [val (oget e :target :value)]
       (my-swap! output-atom k (convert val))))))


(defn input-box []
  [:form
   [two-col-form
    "TX ID"
    [:input.form-control
     {:placeholder "Transaction Hash eg. 5bbd0a1c240dc9df8ba1350eb6142596cbeab32e1ed47db7c0e27d6c7be0ccb6"
      :on-change (input-swap! :txid)
      :type "text"}]]
   [two-col-form
    "Output Index"
    [:input.form-control
     {:default-value 0
      :placeholder "index eg. 0"
      :on-change (input-swap! :index js/parseInt)
      :type "number"}]]
   [two-col-form
    "Amount"
    [:input.form-control
     {:placeholder "Amount eg. 5000000" ; TODO: convert xzc unit
      :on-change (input-swap! :amount (fn [str-amt]
                                         (-> str-amt
                                             (js/parseFloat)
                                             (* 1e8))))
      :type "text"}]]
   [two-col-form
    "Address"
    [:input.form-control
     {:placeholder "Address eg. a95vY5GUY7piVhe9iuPYYd2pysbxJMmJmY"
      :on-change (input-swap! :address)
      :type "text"}]]
   [two-col-form
    "ScriptHash"
    [:input.form-control
     {:placeholder "scriptPubKey.hex eg. 76a9145bd01c12d323d534246b381ff0f8d33e63259aef88ac"
      :on-change (input-swap! :script)
      :type "text"}]]])

(defn output-box []
  [:form
   [two-col-form
    "Address"
    [:input.form-control
     {:placeholder "Destination address"
      :on-change (output-swap! :address)
      :type "text"}]]
   [two-col-form
    "Amount"
    [:input.form-control
     {:placeholder "Send amount"
      :on-change (output-swap! :amount (fn [str-amt]
                                         (-> str-amt
                                             (js/parseFloat)
                                             (* 1e8))))
      :type "text"}]]])

(defn signature-box []
 [:form
  [two-col-form
   "Mnemonic "
   [:input.form-control
    {:placeholder "12 words"
     :on-change (fn [e]
                  (let [val (oget e :target :value)]
                    (swap! signature-atom assoc :mnemonic val)))
     :type "text"}]]

  [two-col-form
   "Password"
   [:input.form-control
    {:placeholder "Password"
     :on-change (fn [e]
                  (let [val (oget e :target :value)]
                    (swap! signature-atom assoc :password val)))
     :type "password"}]]])



(defn result-box [result]
  (let [is-success  (get @result :is-success)
        txblob      (get @result :txblob "-")]
    (prn :result txblob)
    [:form
     [two-col-form
      "Sign result"
      (case is-success
        true
        [:div.alert.alert-success "success"]

        false
        [:div.alert.alert-danger "failed"]

        [:div "-"])]

     [two-col-form
      "Tx Blob"
      [:textarea.form-control-plaintext
       {:rows 10
        :value txblob}]]]))


(defn sign [{:keys [input output signature]}]
  (let [
        {:keys [mnemonic password]} signature

        tx (t/new-transaction)
        tx1 (t/add-input tx input)
        tx2 (t/add-output tx1 output)

        ;; TODO: TODO: Config fee
        ;; TODO: Where the changed go?

        hdpv (k/from-seed (m/to-seed mnemonic password))
        pv (k/derive-key hdpv 0)
        pv-key (k/to-private-key pv)

        tx3 (t/sign-tx tx2 pv-key)]

    (reset!
      result-atom
      {:is-success  (ocall! tx3 :isFullySigned)
       :txblob      (t/get-raw-tx tx3)})))

(defn on-sign-clicked []
  (prn :signing)
  (prn :result (sign {:input @input-atom
                      :output @output-atom
                      :signature @signature-atom})))


(defn page []
  (prn :input @input-atom)
  (prn :output @output-atom)
  (prn :sign @signature-atom)
  [:div
   [:h2 "Sign a transaction"]
   [:div
    [:div.alert.alert-warning "TODO: Auto select input"]
    [:div
     [:h4 "Source/Input"]
     [input-box]]

    [:div
     [:h4 "Destination"]
     [output-box]]

    [:div
     [:h4 "Signing"]
     [signature-box]]

    [:div.text-right
     [:button.btn.btn-success.btn-gradient
      {:on-click on-sign-clicked}
      "sign"]]

    [:div
      [:h4 "Signed Result"]
      [result-box result-atom]]]])


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
