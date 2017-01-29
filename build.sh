SCRIPT_PATH=$(readlink -f $0)
BASE_PATH=`dirname $SCRIPT_PATH`


cd $BASE_PATH/framework
rm -rf $BASE_PATH/framework/target
mvn clean install

cd $BASE_PATH/example-service-clientlib
rm -rf $BASE_PATH/example-service-clientlib/src/main/generated/
rm -rf $BASE_PATH/example-service-clientlib/target
mvn clean install

cd $BASE_PATH/example-service
rm -rf $BASE_PATH/example-service/target
mvn clean install

cd $BASE_PATH/example-client
rm -rf $BASE_PATH/example-client/target
mvn clean install

echo "Done."
