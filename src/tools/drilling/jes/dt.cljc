(ns tools.drilling.jes.dt
  (:require #?(:clj [clojure.core.memoize :as memo])
            [medley.core]
            [tools.drilling.jes.impl :refer [sc->jes-map qkw->spec-kw]]))

(defn dt*
  [sc-reg m]
  (let [i (-> m keys first)]
       (medley.core/map-vals (fn [dtm]
                               (let [{:jes/keys [sc req opt]} dtm
                                     r (into [:jes/id :jes/dt] req)
                                     o (into [:jes/tags :jes/hashtags] opt)]
                                 (as-> dtm $
                                   (merge $ (sc->jes-map sc-reg sc))
                                   (assoc $
                                          :jes/id i
                                          :jes/x? true
                                          :jes/dt? true
                                          :jes/dt :dt/dt
                                          :dt/id i
                                          :jes/name (name i)
                                          :jes/namespace (namespace i)
                                          :jes/sc sc
                                          :jes/select-keys [:jes/id :jes/dt :jes/req :jes/opt :jes/namespace :jes/name :jes/tags :jes/hashtags]
                                          :jes/spec (qkw->spec-kw i)
                                          :jes/raw (assoc dtm :jes/dt :dt/dt)
                                          :jes/req r
                                          :jes/opt o
                                          :jes/fields (into r o))))) m)))

(def dt #?(:clj (memo/memo dt*) :cljs (memoize dt*)))

(defn dts* [m] (->> m seq (map (fn [[i v]] (dt {i v}))) (apply merge)))

(def dts #?(:clj (memo/memo dts*) :cljs (memoize dts*)))

(defn filter-dts [m] (medley.core/filter-keys #(= "dt" (namespace %)) m))
