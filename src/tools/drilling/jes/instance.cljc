(ns tools.drilling.jes.instance
  (:require #?(:clj [clojure.core.memoize :as memo])
            [clojure.set]
            [medley.core]
            [tools.drilling.jes.impl :refer [sc->jes-map qkw->spec-kw]]))

(defn dt-attrs-map [known-attrs-and-dts sc-reg attrs]
  (let [attrs-map-fn (fn [attr]
                       (let [am (-> known-attrs-and-dts attr)]
                         (sc->jes-map sc-reg (:jes/sc am))))
        attrs-map (map (fn [a] {a (attrs-map-fn a)}) attrs)]
    (apply merge attrs-map)))

(defn instance*
  [known-attrs-and-dts sc-reg m]
  (let [i (-> m keys first)]
    (medley.core/map-vals (fn [im]
                            (let [{:jes/keys [sc body]} im
                                  dt (keyword (str "dt/" (namespace i)))
                                  dt-map (get-in known-attrs-and-dts [dt])
                                  dt-fields (:jes/fields dt-map)
                                  dt-attrs (dt-attrs-map known-attrs-and-dts sc-reg dt-fields)
                                  spec (qkw->spec-kw dt)
                                  instance-fields (set (keys im))
                                  instance-fields-in-dt (clojure.set/intersection (set dt-fields)
                                                                                  (set instance-fields))
                                  extra-keys (clojure.set/difference (set dt-fields) (set instance-fields))
                                  unused-keys (clojure.set/difference (set dt-fields) (set instance-fields))
                                  instance-attrs (select-keys dt-attrs instance-fields-in-dt)]
                              (as-> im $
                                (merge $ (sc->jes-map sc-reg sc))
                                (assoc $
                                       :jes/id i
                                       :jes/x? true
                                       :jes/instance? true
                                       :jes/dt dt
                                       :jes/name (name i)
                                       :jes/namespace (namespace i)
                                       :jes/attrs instance-attrs
                                       :jes/extra-attrs extra-keys
                                       :jes/unused-attrs unused-keys
                                       :jes/select-keys dt-fields
                                       :jes/sc sc
                                       :jes/spec spec
                                       :jes/raw (assoc im :jes/dt :dt/dt)
                                       :jes/body body)))) m)))

(def instance #?(:clj (memo/memo instance*)
                 :cljs (memoize instance*)))

(defn instances* [known-attrs-and-dts sc-reg m]
  (->> m seq (map (fn [[i v]] (instance* known-attrs-and-dts sc-reg {i v}))) (apply merge)))

(def instances #?(:clj (memo/memo instances*) :cljs (memoize instances*)))
