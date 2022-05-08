cd "${BASH_SOURCE:-$0:h:h}" || exit
echo Working in $(pwd)
NEXT_VERSION=$(date +"%Y-%m-%d.%H")-SNAPSHOT
echo Updating version to "$NEXT_VERSION"
#mvn versions:set -DnewVersion="$NEXT_VERSION"
#mvn versions:commit