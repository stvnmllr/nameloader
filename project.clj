(defproject loader "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/elastisch "2.2.1"]
                 [net.sourceforge.jtds/jtds "1.3.1"]
                 [org.clojure/java.jdbc "0.6.0-alpha2"]
                 [clj-time "0.8.0"]
                 ]
  :main ^:skip-aot loader.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
