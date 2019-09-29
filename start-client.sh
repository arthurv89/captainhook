BASE_PATH="$( cd "$(dirname "$0")" ; pwd -P )"

(cd $BASE_PATH/main && mvn exec:java)
