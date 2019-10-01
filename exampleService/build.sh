set -x

BASE_PATH="$( cd "$(dirname "$0")" ; pwd -P )"
echo "BASE_PATH: $BASE_PATH"

function e {
	printf "\n\n\n\n\n"

	folder=$1
	cd $BASE_PATH/$folder && \
	rm -rf $BASE_PATH/$folder/src/main/generated-sources/ && \
	rm -rf $BASE_PATH/$folder/target && \
	rm -rf $BASE_PATH/$folder/src/main/resources/plugins && \
	rm -rf $BASE_PATH/$folder/src/main/webapp/WEB-INF/jsp/plugins && \
	mvn clean compile install
}

e framework-core && \
e framework-core-clientlib && \
e framework-plugins-selfdiagnose && \
e framework-core-server && \
e exampleservice-clientlib && \
e exampleservice && \
e exampleservice2-clientlib && \
e exampleservice2 && \
e exampleMiddleService-clientlib && \
e exampleMiddleService && \
e main && \
printf "\n\n\n\n\n\n\n\n\n" && \
echo "---------------------- Done successfully ----------------------" && \
printf "\n\n\n\n\n\n\n\n\n"
