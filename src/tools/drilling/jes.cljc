(ns tools.drilling.jes
  (:require #?(:clj [clojure.core.memoize :as memo])
            [clojure.edn]
            [com.wsscode.pathom3.connect.indexes :as pci]
            [com.wsscode.pathom3.connect.built-in.resolvers :as pbir]
            [com.wsscode.pathom3.interface.smart-map :as psm]
            [medley.core]
            [tools.drilling.jes.group :refer [jes-group]]))

#?(:clj (defn clear-jes-cache
          "This is useful during development. The cache is absolutely helpful in terms of performance, but a pain when developing at the repl."
          [m]
          (memo/memo-clear! jes-group [m])))

(defn build-jes
  "Just a wrapper fn around jes group to match the name with intention."[m]
  (jes-group m))

(defn build-jes-reg
  "Makes a Pathom registry with JES as a static table resolver. If you want to add more resolvers, pass them in as a vector."
  ([jes-map] (pci/register [(pbir/static-table-resolver `jes :jes/id (build-jes jes-map))]))
  ([jes-map rs-and-ms]
   (into (pci/register [(pbir/static-table-resolver `jes :jes/id (build-jes jes-map))])
         rs-and-ms)))

(def ^:dynamic *jes-reg*
  "Place holder for your JES Pathom registry. Make available in the same namespace as where you use sm->"
  {})

(defn with-sm
  "If *jes-reg* is not available, nothing will happen."
  ([] (with-sm *jes-reg*))
  ([selector]
   (psm/smart-map (pci/register *jes-reg*) selector))
  ([added-reg selector]
   (let [r (if (nil? added-reg)
             *jes-reg*
             [*jes-reg* added-reg])]
     (psm/smart-map (pci/register r) selector))))

(defn in-sm
  ([reso] (in-sm {} reso))
  ([sel reso] (in-sm nil sel reso))
  ([added-reg sel reso]
   (-> (with-sm added-reg sel) reso)))

(defn sm->
  "Returns values from the smart map. Dispatches of the type of ident and resolver supplied (map, keyword, or vector)."
  ([reso] (sm-> nil {} reso))
  ([ident reso] (sm-> nil ident reso))
  ([added-reg ident reso]
   (cond

     (and (map? ident) (keyword? reso))
     (in-sm added-reg ident reso)
     
     (and (map? ident) (vector? reso))
     (apply merge (map (fn [r] {r (in-sm added-reg ident r)}) reso))
     
     (and (keyword? ident) (keyword? reso))
     (in-sm added-reg {:jes/id ident} reso)
     
     (and (keyword? ident) (vector? reso))
     (apply merge (map (fn [r] {r (in-sm added-reg {:jes/id ident} r)}) reso))

     (and (vector? ident) (keyword? reso))
     (apply merge (map (fn [i] {i (in-sm added-reg {:jes/id i} reso)}) ident))

     (and (vector? ident) (vector? reso))
     (apply medley.core/deep-merge
            (flatten (for [i ident] (map (fn [r] {i {r (in-sm added-reg {:jes/id i} r)}}) reso))))

     :else (throw (Exception. "sm->: invalid arguments")))))

(defn <-sm
  "Smart maps don't always return values like you'd expect. This strips the extras by making it a string and returns it to being edn again."
  ([sm-value]
  (-> sm-value str clojure.edn/read-string)))
