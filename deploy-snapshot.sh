reldir=`dirname $0`
cd $reldir
directory=`pwd`

cd $directory/framework-core
mvn clean deploy

cd $directory/framework-core-clientlib
mvn clean deploy

cd $directory/framework-core-server
mvn clean deploy

cd $directory/framework-plugins-selfdiagnose
mvn clean deploy
