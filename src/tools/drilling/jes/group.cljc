(ns tools.drilling.jes.group
  (:require #?(:clj [clojure.core.memoize :as memo])
            [clojure.set]
            [medley.core]
            [tools.drilling.jes.attr :as jes.attr]
            [tools.drilling.jes.dt :as jes.dt]
            [tools.drilling.jes.instance :as jes.instance]))

(defn filter-instances [m]
  (let [attr-ids (-> (jes.attr/filter-attrs m) keys set)
        dt-ids (-> (jes.dt/filter-dts m) keys set)]
    (medley.core/filter-keys #(not (contains? (clojure.set/union attr-ids dt-ids) %)) m)))

(defn jes-group* [sc-reg m]
  (let [attr-map (jes.attr/attrs* sc-reg (jes.attr/filter-attrs m))
        dt-map (jes.dt/dts* (jes.dt/filter-dts m))
        instance-map (jes.instance/instances* (merge attr-map dt-map) sc-reg (filter-instances m))]
    (merge attr-map
           dt-map
           instance-map)))

(def jes-group #?(:clj (memo/memo jes-group*)
                  :cljs (memoize jes-group*)))
