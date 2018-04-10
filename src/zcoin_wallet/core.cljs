(ns zcoin-wallet.core
  (:require [webpack.bundle]
            [reagent.core :as reagent :refer [atom]]

            [zcoin-wallet.views.new-mnemonic :as new-mnemonic]
            [zcoin-wallet.views.addr-transactions :as addr-transaction]
            [zcoin-wallet.views.sign-tx :as sign-tx]))

(enable-console-print!)

(println "This text is printed from src/zcoin-wallet/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom 0))


(defn hello-world []
  (prn :app @app-state)
  [:div.container
   [:h1 "Zcoin Wallet"]
   [new-mnemonic/page]
   [:hr]
   [sign-tx/page]
   [:hr]
   [addr-transaction/page]])

(defn on-js-reload []
  (swap! app-state inc))
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
