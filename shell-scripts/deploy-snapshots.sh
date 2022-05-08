cd "${BASH_SOURCE:-$0:h:h}" || exit
PROJECT_ROOT=$(pwd)
echo Starting from "$PROJECT_ROOT"

mvn clean

cd "${PROJECT_ROOT}/execdoc-kb" || exit
cd "${PROJECT_ROOT}/execdoc-core" || exit
cd "${PROJECT_ROOT}/execdoc-apps" || exit

cd "${PROJECT_ROOT}/execdoc-kb"
echo Working in $(pwd)
mvn source:jar deploy || exit

cd "${PROJECT_ROOT}/execdoc-core"
echo Working in $(pwd)
mvn source:jar deploy || exit

cd "${PROJECT_ROOT}/execdoc-apps"
echo Working in $(pwd)
mvn source:jar deploy || exit

cd "${PROJECT_ROOT}/execdoc-spring"
echo Working in $(pwd)
mvn source:jar deploy || exit

cd "${PROJECT_ROOT}/execdoc-jhipster"
echo Working in $(pwd)
mvn source:jar deploy || exit

