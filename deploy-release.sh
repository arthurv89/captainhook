reldir=`dirname $0`
cd $reldir
directory=`pwd`

cd $directory/framework-core
mvn versions:set -DnewVersion=0.1.0
mvn clean deploy -P release

# cd $directory/framework-core-clientlib
# mvn versions:set -DnewVersion=0.1.0
# mvn clean deploy -P release
#
# cd $directory/framework-core-server
# mvn versions:set -DnewVersion=0.1.0
# mvn clean deploy -P release
#
# cd $directory/framework-plugins-selfdiagnose
# mvn versions:set -DnewVersion=0.1.0
# mvn clean deploy -P release
