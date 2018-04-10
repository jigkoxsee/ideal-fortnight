(ns zcoin.mnemonic
  (:require [oops.core :refer [oget oset! ocall oapply ocall! oapply!]]))


(def bip39 (atom nil))

(js/setTimeout (fn [] (reset! bip39 (oget js/window "deps" "bip39"))) 500)


(defn generate []
  """
  Return : string
  """
  (if-not (nil? @bip39)
    (ocall! @bip39 :generateMnemonic)))


(defn to-seed
  ([mnemonic]
   """
   Param : mnemonic - string
   Return : Buffer
   """
   (if-not (nil? @bip39)
     (ocall! @bip39 :mnemonicToSeed mnemonic)))

  ([mnemonic password]
   """
   Param : mnemonic - string, password - string
   Return : Buffer
   """
   (if-not (nil? @bip39)
     ((oget @bip39 :mnemonicToSeed) mnemonic password))))

(defn to-seed-hex [mnemonic]
  """
  Param : mnemonic - string
  Return : String
  """
  (if-not (nil? @bip39)
    (ocall! @bip39 :mnemonicToSeedHex mnemonic)))

(defn validate [mnemonic]
  (if-not (nil? @bip39)
    (ocall! @bip39 :validateMnemonic mnemonic)))

(comment

  js/window.deps
  (js/Object.keys @bip39)
  (js/Object.keys (oget js/window "deps" "bip39"))

  (generate)
  (to-seed (generate))
  (to-seed-hex (generate)))
