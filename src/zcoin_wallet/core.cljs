(ns zcoin-wallet.core
    (:require [webpack.bundle]
              [reagent.core :as reagent :refer [atom]]
              [zcoin.key :as k]
              [zcoin.transaction :as t]
              [zcoin.mnemonic :as m]
              [zcoin-wallet.views.sign-tx :as sign-tx]))

(enable-console-print!)

(println "This text is printed from src/zcoin-wallet/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:mnemonic "-"}))

(defn on-mnemonic-input-change [e]
  (let [val (-> e .-target .-value)]
    (prn :mnemonic-changed val)
    (swap! app-state assoc :mnemonic val)))

(defn mnemonic-box []
  (let [mnemonic (:mnemonic @app-state)
        is-valid-mnemonic (m/validate mnemonic)]

    [:div
     [:h3 "Mnemonic"]

     [:button.btn.btn-success
      {:on-click (fn [] (swap! app-state assoc :mnemonic (m/generate)))}
      "Random"]

     [:div
      [:input {:type "text"
               :style {:width 800}
               :on-change on-mnemonic-input-change
               :value mnemonic}]
      [:p "Mnemonic: " mnemonic]
      [:p "Is valid mnemonic?: " (if is-valid-mnemonic "yes" "nooo")]


      (if is-valid-mnemonic
        (let [seed (m/to-seed mnemonic)
              hdpv (k/from-seed seed)
              pv (k/derive-key hdpv 0)

              private-key (k/to-wif pv)
              public-key (k/to-public-key pv)
              address (k/to-address pv)]
          [:div
           [:p "Private Key: " private-key]
           [:p "Public Key: " public-key]
           [:p "Address: " address]]))]]))

(defn hello-world []
  [:div.container
   [:h1 (:text @app-state)]
   [:h2 "Hello Zcoin"]
   [:h3 "Edit this and watch it change!"]
   [:div
    [:h5 :mnemonic]
    [:div.btn.btn-success "wow"]
    [:pre>p "ww"]];(m/generate)]]])
   [:hr]
   [mnemonic-box]
   [:hr]
   [sign-tx/page]])

(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)


(defn mount-root []
  (reagent/render [hello-world] (. js/document (getElementById "app"))))

(defn ^:export init []
  (mount-root))


(comment

  (swap! app-state assoc :mnemonic "1234")

  (m/generate)
  (js/Object.keys js/window.deps)
  (js/Object.keys (aget js/window.deps "bip39")))
