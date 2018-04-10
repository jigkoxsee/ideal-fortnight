(ns zcoin.key
  (:require
    [zcoin.mnemonic :as m]
    [oops.core :refer [oget oset! ocall oapply ocall! oapply!]]))


(def zcoin-lib (atom ((oget js/window "require") "bitcore-lib")))


; Constant derive path for zcoin
(def derived-path "m/44'/136'/")
(def default-account 0)

;(def default-account 1)


(defn get-derive-path [account index]
  ;(let [path (str derived-path account "/" index)])
  (let [path (str derived-path account "'/0/" index)]
    ;(prn :derive-path path)
    path))

;; TODO: Make it a proper test
(defn test-get-derive-path []
  "test by derive second address of 1 user - 0/1"
  (= "m/44'/136'/0'/0/1" (get-derive-path "0" "1")))

(defprotocol HDKey
  "HD Key Protocal"
  (derive-key [this index]))

(defprotocol IPrivateKey
  "Private Key Protocal"
  (to-wif [this])
  (to-private-key [this])
  (to-public-key [this])
  (to-address [this]))


(defrecord PrivateKey [pv]
  IPrivateKey
  (to-wif [{:keys [pv]}]
    (ocall! pv :toWIF))
  (to-private-key [{:keys [pv]}]
    (ocall! pv :toString))
  (to-public-key [{:keys [pv]}]
    (-> pv
        (oget :publicKey)
        (ocall! :toString)))
  (to-address [{:keys [pv]}]
    (-> pv
        (ocall! :toAddress)
        (ocall! :toString))))


(defrecord HDPrivateKey [hdpv]
  HDKey
  (derive-key [{:keys [hdpv]} index]
    """
    Derive private key of account 0 at <index>
    """
    (-> hdpv
       (ocall! :derive (get-derive-path default-account index))
       (oget :privateKey)
       (->PrivateKey))))


(defn from-seed [seed]
  (if-not (nil? @zcoin-lib)
    (let [f (oget @zcoin-lib :HDPrivateKey :fromSeed)]
      (->HDPrivateKey (f seed)))))


(comment

  (def lib ((oget js/window "require") "bitcore-lib"))


  ((oget lib "PrivateKey"))


  (get-derive-path "0" "1")
  (test-get-derive-path)

  (let [seed (m/to-seed (m/generate))
        hdpv (from-seed seed)
        pv (derive-key hdpv 0)]
    (prn :keys (js/Object.keys (:pv pv)))
    (prn :pubkey (js/Object.keys (oget (:pv pv) "_pubkey")))
    (prn :wif (to-wif pv))
    (prn :private (to-private-key pv))
    (prn :public (to-public-key pv))
    (to-address pv)))

