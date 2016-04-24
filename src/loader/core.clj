(ns loader.core
  (:require [clojure.java.jdbc :as sql]
            [clojure.string :as str]
            [clj-time.core :as t])
  (:gen-class))

(def db-spec 
  {:classname "net.sourceforge.jtds.jdbc.Driver"
   :subprotocol "jtds:sqlserver"
   :subname "//localhost:1433/37NS"
   :user "webuser"
   :password "password"})

(defn randSsn []
  (format "%03d%02d%04d" (rand-int 1000) (rand-int 100) (rand-int 10000)))

(defn randDate []  
  (.toDate (t/minus (t/today) (t/days (rand-int (* 365 85))))))

(defn now []
    (java.util.Date.))

(defn sqldate [d]
  (java.sql.Timestamp. (.getTime d))) 

(defn get-lines [file]
  (str/split-lines (slurp file)))

(def firstList (get-lines "resources\\first.csv"))
(def lastList (get-lines "resources\\last.csv"))

(def nextNum  (first (sql/query db-spec 
  ["select max(recnum) cost from mastname"]
  {:row-fn :cost})))

(def aliasSn  (first (sql/query db-spec 
  ["select max(recnum) cost from alias_h"]
  {:row-fn :cost})))

(def historySn  (first (sql/query db-spec 
  ["select max(recnum) cost from mastname_history"]
  {:row-fn :cost})))

(defn- chance [f i]
  (if (= 0 (rand-int (+ 1 i)))
             (f)
             nil))

(defonce counter (atom nextNum))
(defonce aliasSq (atom aliasSn))
(defonce historySq (atom historySn))

(defn makeName [] 
  (swap! counter inc)
  (let [bday (randDate)
        age (t/in-years (t/interval 
                          (org.joda.time.DateTime. bday)
                          (org.joda.time.DateTime. (now))))]
    {:recnum @counter 
     :fname (str/upper-case (rand-nth firstList))
     :lname (str/upper-case (rand-nth lastList))
     :dob (sqldate bday) 
     :age age
     :ssn (chance #(randSsn) 1)
     :agency_rec 20
     :created_by "steve" 
     :created_on (sqldate (now))}))

(defn makeAlias [nameRecnum]
  (swap! aliasSq inc)
  {:recnum @aliasSq
   :name_rec nameRecnum
   :fname (str/upper-case (rand-nth firstList))
   :lname (str/upper-case (rand-nth lastList))
   :middle (chance #(str/upper-case (rand-nth firstList)) 1) 
   :ssn (chance #(randSsn) 1)
   :agency_rec 20
   :created_by "steve" 
   :created_on (sqldate (now))   
   })

(defn makeHistory [nameRecnum]
  (swap! historySq inc)
  {:recnum @historySq
   :name_rec nameRecnum
   :fname (chance #(str/upper-case (rand-nth firstList)) 1)
   :lname (chance #(str/upper-case (rand-nth lastList)) 1)
   :middle (chance #(str/upper-case (rand-nth firstList)) 1) 
   :type "NAME"
   :agency_rec 20
   :created_by "steve" 
   :created_on (sqldate (now))   
   })

(defn insertMany [n]
  (let [names (take n (repeatedly makeName))
        recnums (map :recnum names)
        recsForAlias (random-sample 0.3 recnums)
        recsForHistory (random-sample 0.15 recnums)
        historyMaps (map makeHistory recsForHistory) 
        aliases (map makeAlias recsForAlias)]
    (sql/insert-multi! db-spec :mastname names)
    (sql/insert-multi! db-spec :alias_h aliases)
    (sql/insert-multi! db-spec :mastname_history historyMaps)
    )) 

(defn -main
  [& args]
  (dotimes [n 500]
    (insertMany 1000))
  (println "created data"))

