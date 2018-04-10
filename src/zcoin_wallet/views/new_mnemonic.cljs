(ns zcoin-wallet.views.new-mnemonic
  (:require
    [reagent.core :as r :refer [atom]]
    [oops.core :refer [oget oget+ oset! ocall oapply ocall! oapply!]]
    [ui.core :as ui]
    [zcoin.key :as k]
    [zcoin.transaction :as t]
    [zcoin.mnemonic :as m]))

(defonce app-state (atom {:mnemonic "-"}))

(defn on-mnemonic-input-change [e]
  (let [val (oget e :target :value)]
    (prn :mnemonic-changed val)
    (swap! app-state assoc :mnemonic val)))

(defn page []
  (let [mnemonic (:mnemonic @app-state)
        is-valid-mnemonic (m/validate mnemonic)]

    [:div
     [:h2 "Mnemonic"]

     [:form

      [ui/two-col-form
       "Mnemonic"
       [:input.form-control
        {:on-change on-mnemonic-input-change
         :value mnemonic
         :type "text"}]]


      [ui/two-col-form
       "Or "
       [:button.btn.btn-success.btn-gradient
        {:on-click (fn [e]
                     (ocall! e :preventDefault)
                     (swap! app-state assoc :mnemonic (m/generate)))}
        "Generate Random Mnemonic"]]

      [ui/two-col-form
       "Is valid Mnemonic?"
       (if is-valid-mnemonic
         [:div.alert.alert-success "Valid"]
         [:div.alert.alert-danger "Invalid"])]



      (if is-valid-mnemonic
        (let [seed (m/to-seed mnemonic)
              hdpv (k/from-seed seed)
              pv (k/derive-key hdpv 0)

              private-key (k/to-wif pv)
              public-key (k/to-public-key pv)
              address (k/to-address pv)]
          [:div
           [ui/two-col-form
            "Private Key"
            [:input.form-control
             {:value private-key
              :type "text"}]]
           [ui/two-col-form
            "Public Key"
            [:input.form-control
             {:value public-key
              :type "text"}]]
           [ui/two-col-form
            "Address"
            [:input.form-control
             {:value address
              :type "text"}]]]))]]))
