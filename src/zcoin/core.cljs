(ns zcoin.core
  (:require
    [zcoin.key :as k]
    [zcoin.mnemonic :as m]
    [oops.core :refer [oget oset! ocall oapply ocall! oapply!]]))


(def bip39 (aget js/window "deps" "bip39"))
(def zcoin-lib (aget js/window "deps" "zcoin-lib"))
;(def bitcoin (aget js/window "deps" "bitcoinjs"))


(defn from-seed [seed])
(defn to-seed [mnemonic])

(comment
  (.toWIF (bitcoin.ECPair.makeRandom))
  (ocall! bitcoin "ECPair" "makeRandom")
  (ocall! (ocall! bitcoin :ECPair :makeRandom) :toWIF)
  (oget zcoin-lib :HDPrivateKey :fromSeed)
  (ocall! zcoin-lib :HDPrivateKey :fromBu "")
  (oget zcoin-lib :HDPrivateKey :fromSeed)
  ((oget zcoin-lib :HDPrivateKey :fromSeed) (ocall! bip39 :mnemonicToSeedHex (ocall! bip39 :generateMnemonic)))
  ((oget zcoin-lib :HDPrivateKey :fromSeed) (ocall! bip39 :mnemonicToSeed (ocall! bip39 :generateMnemonic)))
  (ocall! zcoin-lib :HDPrivateKey :fromSeed (ocall! bip39 :mnemonicToSeedHex (ocall! bip39 :generateMnemonic)))
  (goog/typeOf (ocall! bip39 :mnemonicToSeed (ocall! bip39 :generateMnemonic)))
  (ocall! bip39 :mnemonicToSeedHex (ocall! bip39 :generateMnemonic))
  (ocall! bip39 :mnemonicToSeed (ocall! bip39 :generateMnemonic)))

