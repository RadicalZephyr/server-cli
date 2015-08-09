(set-env!
 :source-paths #{"src"}
 :dependencies '[[radicalzephyr/boot-junit "0.2.1"  :scope "test"]
                 [junit                    "4.12"   :scope "test"]
                 [radicalzephyr/http-server "0.1.0-SNAPSHOT"]
                 [net.sf.jopt-simple/jopt-simple "4.9"]])

(require '[radicalzephyr.boot-junit :refer [junit]])

(def +version+ "0.1.0-SNAPSHOT")

(task-options!
 pom {:project 'radicalzephyr/http-server
      :version +version+
      :description "The command line program for a Java HTTP server."
      :url "https://github.com/RadicalZephyr/server-cli"
      :scm {:url "https://github.com/RadicalZephyr/server-cli.git"}
      :licens {"MIT" "http://opensource.org/licenses/MIT"}}
 jar {:file "server-cli.jar"
      :manifest {"Main-Class" "server_cli.StartServer"}}
 sift {:invert true
       :include #{#"\.java$"}}
 junit {:classes #{"SmokeTest"}})

(deftask build
  "Build my http server."
  []
  (comp (javac)
        (pom)
        (sift)
        (uber)
        (jar)))
