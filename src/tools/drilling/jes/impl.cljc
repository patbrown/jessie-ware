(ns tools.drilling.jes.impl)

(defn qkw->spec-kw [k]
  (let [nmsp (namespace k)
        n (name k)]
    (keyword (str "spec." nmsp) n)))

(defn remove-nils [seq]
  (->> seq (remove nil?)))

(defn sc->jes-map [sc-reg sc-vec]
  (apply merge (remove-nils (map #(% sc-reg) sc-vec))))

(defn determine-vt [sc]
  (first (filter #{:double :inst :int :ref :float :kw :sym :str :bytes :uri :uuid :boolean} sc)))
