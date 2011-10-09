(ns $unitname$
  (:require [clj-rpc.client :as client]))

;;the hostaddress and port of the url
;;like http://localhost:8080
(def ^dynamic *invoke-url*)

(defn- invoke-url
  "get invoke url
   return like http://localhost:8080/invoke"
  []
  (str *invoke-url* "/invoke"))

