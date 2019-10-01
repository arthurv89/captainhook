rm -r $captainHookProject/captainhook/tutorial/helloMoonService
rm -r $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib

cp -r $captainHookProject/captainhook/tutorial/helloWorldService $captainHookProject/captainhook/tutorial/helloMoonService
cp -r $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib

rm -rf $captainHookProject/captainhook/tutorial/helloMoonService/src/main/generated-sources
rm -rf $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/generated-sources

mv $captainHookProject/captainhook/tutorial/helloMoonService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/helloworld/HelloWorldActivity.java $captainHookProject/captainhook/tutorial/helloMoonService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/helloworld/HelloMoonActivity.java
mv $captainHookProject/captainhook/tutorial/helloMoonService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/helloworld/ $captainHookProject/captainhook/tutorial/helloMoonService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/hellomoon/
mv $captainHookProject/captainhook/tutorial/helloMoonService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/ $captainHookProject/captainhook/tutorial/helloMoonService/src/main/java/com/arthurvlug/captainhook/tutorial/hellomoonservice/

mv $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloWorldInput.java $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloMoonInput.java
mv $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloWorldOutput.java $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloMoonOutput.java
mv $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/ $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/hellomoon/
mv $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/hellomoonservice

cd $captainHookProject/captainhook/tutorial/helloMoonService/
perl -p -i -e 's/world/moon/g' `find . -type f`
perl -p -i -e 's/World/Moon/g' `find . -type f`
perl -p -i -e 's/8080/8081/g' `find . -type f`

cd $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib/
perl -p -i -e 's/world/moon/g' `find . -type f`
perl -p -i -e 's/World/Moon/g' `find . -type f`
perl -p -i -e 's/8080/8081/g' `find . -type f`
