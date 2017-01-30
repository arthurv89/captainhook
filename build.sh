SCRIPT_PATH=$(readlink -f $0)
BASE_PATH=`dirname $SCRIPT_PATH`

function e {
	folder=$1
	cd $BASE_PATH/$folder && \
	rm -rf $BASE_PATH/$folder/src/main/generated/ && \
	rm -rf $BASE_PATH/$folder/target && \
	rm -rf $BASE_PATH/$folder/src/main/resources/plugins && \
	rm -rf $BASE_PATH/$folder/src/main/webapp/WEB-INF/jsp/plugins && \
	mvn clean install
}

e framework-core \ &&
e framework-plugins-selfdiagnose \ &&
e example-service-clientlib \ &&
e example-service \ &&
e example-client

echo "Done."
