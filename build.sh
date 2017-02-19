SCRIPT_PATH=$(readlink -f $0)
BASE_PATH=`dirname $SCRIPT_PATH`

function e {
	printf "\n\n\n\n\n"

	folder=$1
	cd $BASE_PATH/$folder && \
	rm -rf $BASE_PATH/$folder/src/main/generated-sources/ && \
	rm -rf $BASE_PATH/$folder/target && \
	rm -rf $BASE_PATH/$folder/src/main/resources/plugins && \
	rm -rf $BASE_PATH/$folder/src/main/webapp/WEB-INF/jsp/plugins && \
	mvn compile clean install
}

e framework-core && \
e framework-core-clientlib && \
e framework-core-server && \
e framework-plugins-selfdiagnose && \
e exampleservice-clientlib && \
e exampleservice && \
e exampleservice2-clientlib && \
e exampleservice2 && \
e exampleMiddleService-clientlib && \
e exampleMiddleService && \
e main && \
printf "\n\n\n\n\n\n\n\n\n" && \
echo "---------------------- Done successfully ----------------------"


printf "\n\n\n\n\n\n\n\n\n"

