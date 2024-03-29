(ns leiningen.gen.gen-file
  (:require [clj-rpc.client :as client]
            [clojure.string :as string]))

;;template file name of function body
(defonce template-fn-body "fn_body.clj")

;;template file name of function
(defonce template-fn "fn_stub.clj")

;;template file name the output file
(defonce template-file "api_stub.clj")

(defn- slurp-resource*
  [resource-name]
    (-> (.getContextClassLoader (Thread/currentThread))
        (.getResourceAsStream resource-name)
        (java.io.InputStreamReader.)
        (slurp)))

;;From the maginalia source: http://fogus.me/fun/marginalia/
(defn slurp-resource
  [resource-name]
  (try
    (slurp-resource* resource-name)
    (catch java.lang.NullPointerException npe
      (println (str "Could not locate resources at " resource-name))
      (println "    ... attempting to fix.")
      (let [resource-name (str "./resources/" resource-name)]
        (try
          (slurp-resource* resource-name)
          (catch java.lang.NullPointerException npe
            (println (str "    STILL could not locate resources at " resource-name ". Giving up!"))))))))


(defn- change-args [args]
  (let [args-str (pr-str args)
        last-pos (- (.length args-str) 1)]
    (.substring args-str 1 last-pos)))

(defn- replace-symbol
  [str replace-map]
  (reduce (fn [s [sym replacement]] (string/replace s sym replacement))
          str replace-map))

(defn replace-parameters
  "replace all the fn-name and args in the template with fn and args"
  [str fn-name args]
  (let [apply-str (if (= (symbol "&") (last (drop-last args)))
                "apply "
                "")
        to-changes (filter #(not= % (symbol "&")) args)]
    (replace-symbol str
                    {"$fn-name$" fn-name,
                     "$args$" (change-args args),
                     "$changed-args$" (change-args to-changes),
                     "$apply$" apply-str})))

(defn gen-fn-body
  "generate string of the function body"
  [template-path command]
  (let [content (string/trimr (slurp-resource
                               (str template-path template-fn-body)))
        fn-name (:name command)
        argslist (read-string (:arglists  command))]
    (->> argslist 
        (map (partial replace-parameters content fn-name))
        (string/join "\n"))))

(defn gen-fn
  "generate string of function"
  [template-path command]
  (let [content (slurp-resource (str template-path template-fn))
        fn-name (:name command)
        doc (or (:doc command) "")
        doc (string/replace doc "\"" "\\\"")]
    (replace-symbol content
                    {"$fn-name$" fn-name
                     "$doc$" doc
                     "$body$" (gen-fn-body template-path command)})))

(defn gen-file
  "generate string of file"
  [ns template-path commands]
  (let [content (slurp-resource (str template-path template-file))
        fns (map (partial gen-fn template-path) commands)]
    (-> content
        (string/replace "$unitname$" (str ns) )
        (str (string/join "\n" fns)))))

(defn generate
  "write stub file to f according to the template files
   and help"
  [f ns template-path help-url]
  (let [commands (client/help help-url)]
    (spit f (gen-file ns template-path commands))))
