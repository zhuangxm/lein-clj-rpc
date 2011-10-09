(ns leiningen.gen-stub
  (:use [leiningen.help :only (help-for)])
  (:require [leiningen.util.paths :as paths]
            [clojure.java.io :as io]
            [leiningen.gen.gen-file :as gen-file]))

(defn gen-stub
  "using the help service of server to generate the api stub for development
   usage : lein gen-stub namespace serveraddress serverport
   example : lein gen-stub com.basecity.api 127.0.0.1 8080
   serveraddress and serverport can be optional
   the default values are 127.0.0.1 and 8080
   this example will generate a file name api.clj under the
   src/com/basecity/ direction"
  ([project]
     (println (help-for "gen-stub") ))
  ([project ns]
     (gen-stub project ns "127.0.0.1" "8080"))
  ([project ns serveraddress]
     (gen-stub project ns serveraddress "8080"))
  ([project ns serveraddress port]
     (let [ns (create-ns (symbol ns))
           f (paths/ns->path ns)
           ab-f (str (:source-path project) "/" f)]
       (io/make-parents ab-f)
       (gen-file/generate ab-f ns
                          "templates/" 
                          (str "http://" serveraddress ":" port "/help"))
       (println "generated file : " ab-f))))
