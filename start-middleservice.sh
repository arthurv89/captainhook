BASE_PATH="$( cd "$(dirname "$0")" ; pwd -P )"

(cd $BASE_PATH/exampleMiddleService && mvn -Prun exec:java)
