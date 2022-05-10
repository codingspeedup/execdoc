cd "${BASH_SOURCE:-$0:h:h}" || exit
PROJECT_ROOT=$(pwd)
echo Starting from "$PROJECT_ROOT"

mvn clean
mvn source:jar deploy || exit


