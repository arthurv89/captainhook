SCRIPT_PATH=$(readlink -f $0)
BASE_PATH=`dirname $SCRIPT_PATH`

(cd $BASE_PATH/exampleservice && mvn -Prun exec:java)
