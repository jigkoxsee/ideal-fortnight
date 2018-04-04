(ns zcoin.mnemonic
  (:require [oops.core :refer [oget oset! ocall oapply ocall! oapply!]]))

(def bip39 (aget js/window "deps" "bip39"))


(defn generate []
  """
  Return : string
  """
  (ocall! bip39 :generateMnemonic))


(defn to-seed
  ([mnemonic]
   """
   Param : mnemonic - string
   Return : Buffer
   """
   (ocall! bip39 :mnemonicToSeed mnemonic))

  ([mnemonic password]
   """
   Param : mnemonic - string, password - string
   Return : Buffer
   """
   ((oget bip39 :mnemonicToSeed) mnemonic password)))

(defn to-seed-hex [mnemonic]
  """
  Param : mnemonic - string
  Return : String
  """
  (ocall! bip39 :mnemonicToSeedHex mnemonic))

(comment
  (generate)
  (to-seed (generate))
  (to-seed-hex (generate)))
