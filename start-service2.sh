BASE_PATH="$( cd "$(dirname "$0")" ; pwd -P )"

(cd $BASE_PATH/exampleservice2 && mvn -Prun exec:java)
