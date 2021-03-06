(ns arachne.core.config.dsl-test
  (:require [clojure.test :refer :all]
            [arachne.core :as core]
            [arachne.core.config :as cfg]
            [arachne.core.config.init :as dsl]))

(deftest basic-dsl
  (let [forms '[(arachne.core.config.init/update
                  (fn [cfg]
                    (arachne.core.config/update cfg
                      [{:db/id (arachne.core.config/tempid) :arachne/id :dsl-test-1}])))
                (arachne.core.config.init/update
                  (fn [cfg]
                    (arachne.core.config/update cfg
                      [{:db/id (arachne.core.config/tempid) :arachne/id :dsl-test-2}])))]
        cfg (apply core/build-config '[:org.arachne-framework/arachne-core] [] forms)]
    (is (not-empty
          (cfg/q cfg '[:find ?e
                       :where
                       [?e :arachne/id :dsl-test-1]])))
    (is (not-empty
          (cfg/q cfg '[:find ?e
                       :where
                       [?e :arachne/id :dsl-test-2]])))))

(deftest throws-when-called-from-normal-code
  (is (thrown-with-msg? Throwable #"outside of a configuration"
        (dsl/update [{:db/id (arachne.core.config/tempid)
                      :arachne/id :dsl-test-1}]))))
