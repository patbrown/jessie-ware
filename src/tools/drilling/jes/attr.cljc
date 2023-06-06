(ns tools.drilling.jes.attr
  (:require #?(:clj [clojure.core.memoize :as memo])
            [clojure.set]
            [medley.core]
            [tools.drilling.jes.impl :refer [sc->jes-map determine-vt]]))

(defn attr* [sc-reg m]
  (let [i (-> m keys first)]
    (medley.core/map-vals (fn [{:as am :jes/keys [spec sc]}]
                            (as-> am $
                              (merge $ (sc->jes-map sc-reg sc))
                              (dissoc $ :spec :sc)
                              (assoc $
                                     :jes/id i
                                     :jes/attr? true
                                     :jes/x? true
                                     :jes/vt (determine-vt sc)
                                     :jes/name (name i)
                                     :jes/namespace (namespace i)
                                     :jes/sc sc
                                     :jes/spec spec
                                     :jes/raw am
                                     :jes/dt :dt/attr)
                              (if (set? spec)
                                (assoc $ :jes/enum? true)
                                $)))
                          m)))

(def attr #?(:clj (memo/memo attr*) :cljs (memoize attr*)))

(defn attrs* [sc-reg m] (->> m seq (map (fn [[i v]] (attr* sc-reg {i v}))) (apply merge)))

(def attrs #?(:clj (memo/memo attrs*) :cljs (memoize attrs*)))

(defn filter-attrs [m]
  (medley.core/filter-vals
   #(not (empty? (clojure.set/intersection #{:one :many} (-> % :jes/sc set)))) m))
