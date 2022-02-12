cd ${BASH_SOURCE:-$0:h:h}/execdoc-core
mvn clean deploy
cd ${BASH_SOURCE:-$0:h:h}/execdoc-apps
mvn clean deploy
cd ${BASH_SOURCE:-$0:h:h}/execdoc-jhipster
mvn clean deploy