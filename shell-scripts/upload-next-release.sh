cd "${BASH_SOURCE:-$0:h:h}" || exit
PROJECT_ROOT=$(pwd)
echo Working in "$PROJECT_ROOT"

VERSION="2022-11-01"
echo Version is \`"${VERSION}"\'
mvn versions:set -DnewVersion="${VERSION}"

mvn clean source:jar deploy # -Dmaven.test.skip=true