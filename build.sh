SCRIPT_PATH=$(readlink -f $0)
BASE_PATH=`dirname $SCRIPT_PATH`

function e {
	folder=$1
	cd $BASE_PATH/$folder && \
	rm -rf $BASE_PATH/$folder/src/main/generated-sources/ && \
	rm -rf $BASE_PATH/$folder/target && \
	rm -rf $BASE_PATH/$folder/src/main/resources/plugins && \
	rm -rf $BASE_PATH/$folder/src/main/webapp/WEB-INF/jsp/plugins && \
	mvn compile clean install
	printf "\n\n\n\n\n"
}

e framework-core && \
e framework-plugins-selfdiagnose && \
e exampleservice-clientlib && \
e exampleservice && \
e exampleservice2-clientlib && \
e exampleservice2 && \
e exampleMiddleService-clientlib && \
e exampleMiddleService && \
e main

echo "Done."
