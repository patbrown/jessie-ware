(ns tools.drilling.jes.macros
  (:require [clojure.spec.alpha]
            [tools.drilling.jes.impl]))

;;; DEFATTR
(defmacro defattr-spec
  ([m] (let [i# (first (keys m)), m# (first (vals m))]
         (do `(defattr-spec ~i# ~m#))))
  ([i m]
   (let [spec# (:jes/spec m)]
    `(do
       (clojure.spec.alpha/def ~(tools.drilling.jes.impl/qkw->spec-kw i) ~spec#)))))

(defmacro defdt-spec
  ([m] (let [i# (first (keys m)), m# (first (vals m))]
         (do `(defdt-spec ~i# ~m#))))
  ([i m] (let [req# (if (contains? (set (:jes/req m)) :jes/dt)
                      (:jes/req m)
                      (into [:jes/dt] (:jes/req m)))
               opt# (into [:jes/tags :jes/hashtags] (:jes/opt m))]
           `(do (clojure.spec.alpha/def ~(tools.drilling.jes.impl/qkw->spec-kw i)
                  (clojure.spec.alpha/keys :req ~req# :opt ~opt#))))))

(defmacro functionize [macro]
  `(fn [& args#] (eval (cons '~macro args#))))

(defmacro apply-macro [macro args]
   `(apply (functionize ~macro) ~args))

(defmacro scaffold-macro [macro m]
  `(let [v# (mapv (fn [i#] {(first i#) (second i#)}) ~m)]
     (for [instance# v#]
       (apply-macro ~macro [instance#]))))
