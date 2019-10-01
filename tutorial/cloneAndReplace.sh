rm -rf $captainHookProject/captainhook/tutorial/hellomoonservice
rm -rf $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib

cp -r $captainHookProject/captainhook/tutorial/helloworldservice $captainHookProject/captainhook/tutorial/hellomoonservice
cp -r $captainHookProject/captainhook/tutorial/helloworldservice-clientlib $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib

rm -rf $captainHookProject/captainhook/tutorial/hellomoonservice/src/main/generated-sources
rm -rf $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/generated-sources

mv $captainHookProject/captainhook/tutorial/hellomoonservice/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/helloworld/HelloWorldActivity.java $captainHookProject/captainhook/tutorial/hellomoonservice/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/helloworld/HelloMoonActivity.java
mv $captainHookProject/captainhook/tutorial/hellomoonservice/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/helloworld/ $captainHookProject/captainhook/tutorial/hellomoonservice/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/hellomoon/
mv $captainHookProject/captainhook/tutorial/hellomoonservice/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/ $captainHookProject/captainhook/tutorial/hellomoonservice/src/main/java/com/arthurvlug/captainhook/tutorial/hellomoonservice/

mv $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloWorldInput.java $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloMoonInput.java
mv $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloWorldOutput.java $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloMoonOutput.java
mv $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/ $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/hellomoon/
mv $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/src/main/java/com/arthurvlug/captainhook/tutorial/hellomoonservice

cd $captainHookProject/captainhook/tutorial/hellomoonservice/
perl -p -i -e 's/world/moon/g' `find . -type f`
perl -p -i -e 's/World/Moon/g' `find . -type f`
perl -p -i -e 's/8080/8081/g' `find . -type f`

cd $captainHookProject/captainhook/tutorial/hellomoonservice-clientlib/
perl -p -i -e 's/world/moon/g' `find . -type f`
perl -p -i -e 's/World/Moon/g' `find . -type f`
perl -p -i -e 's/8080/8081/g' `find . -type f`

echo "Done"
