(defproject loader "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [net.sourceforge.jtds/jtds "1.3.1"]
                 [org.clojure/java.jdbc "0.6.0-alpha2"]
                 [clj-time "0.8.0"]
                 [hikari-cp "1.6.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.2"]
                 [com.microsoft.sqlserver/sqljdbc4 "4.0"]
                 [com.oracle/ojdbc6 "11.2.0.3.0"]
                 ]
  :repositories [["mylocal" "http://aspen:8081/artifactory/ext-release-local/"]]
  ; :main ^:skip-aot loader.core
  :main loader.core
  :target-path "target/%s"
  :aot :all
  :profiles {:uberjar {:aot :all}})
