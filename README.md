# Captain Hook

## Introduction

Captain Hook is an API framework that makes sure the client and the server use the same contract to communicate.
The server generates helper classes so clients can use that to do calls to the server without any configuration.
Because the server knows the domain model, the helper classes not only take care of calling the server but also take care of serializing/deserializing the objects so the client doesn't have to do that.


## Project structure and responabilities

First check out the git project:
```bash
export captainHookProject=~/workspace/captainhook-tutorial
mkdir -p $captainHookProject
git clone git@github.com:arthurv89/captainhook.git
```

For each service we need 2 modules: a "server" and a "clientlib".

### The server module
The server module implements all the necessary functionalities for that service.
In order to handle requests, you need to define an Activity class.

Activities are similar to Spring Controllers and simply do business logic and return a response.
An Activity only handles one type of request.
It receives a single parameter (which can be a complex object by itself) and returns a simple response (wrapped in an Observable type).

### The client module
The client module is the module that gets shared with consuming services.
This is the reason it has to be split from the service' business logic: this way it's way smaller and also we don't expose anything that consumers shouldn't know about.

The module should expose a Client class and the service's domain classes.
These classes are enough to make type-checked calls to a server without needing any configurations on the consumer side (apart from adding the clientlib as a dependency in the project).

## How to get started
It's easiest to start with the clientlib module.

### Step 1: Create a clientlib module:
In this document we are going to create a service called HelloWorldService

### Add parent in pom.xml
First, create a clientlib project and add the following parent:
```bash
mkdir -p $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib
touch $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib/pom.xml
```

Enter the following:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <artifactId>helloworldservice-clientlib</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>helloworldservice-clientlib</name>

    <parent>
        <groupId>com.arthurvlug.captainhook</groupId>
        <artifactId>framework-core-clientlib</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>2.5.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Add Input and output classes

The Activity input class can be anything you want, as long as it inherits from the Input class.

In the example below, we used Lombok to add getters and a builder to generate this object in a clean way, and to get it's parameters without creating the getter methods ourselves.
```bash
mkdir -p $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld
touch $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloWorldInput.java
```

Edit the file with the following contents:
```java
package com.arthurvlug.captainhook.tutorial.helloworldservice.activity.helloworld;

import lombok.Builder;
import lombok.Getter;
import com.arthurvlug.captainhook.framework.server.Input;

@Builder
@Getter
public class HelloWorldInput extends Input {
    private final String name;
}
```

The Activity output class is similar to the input class: you have all the freedom define it, as long as you set it's parent class: to Output.
 
```bash
touch $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/activity/helloworld/HelloWorldOutput.java
```

Edit the file with the following contents:
```java
package com.arthurvlug.captainhook.tutorial.helloworldservice.activity.helloworld;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import com.arthurvlug.captainhook.framework.common.response.Output;

import java.time.Instant;

@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class HelloWorldOutput extends Output {
    private String message;
    private Instant respondingTime;
}
```

Lastly, in the resources folder, create a file called application.properties and add the following lines:

```bash
mkdir -p $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib/src/main/resources
touch $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib/src/main/resources/application.properties
```

Edit the file with the following contents:
```
name = Hello World Service
server.port = 8080
```

With the clientlib in place, we can now move on to the server module.


### Step 2: Create a server module:

```bash
mkdir -p $captainHookProject/captainhook/tutorial/helloWorldService
touch $captainHookProject/captainhook/tutorial/helloWorldService/pom.xml
```

Again, after creation of the server module, let's change the pom.xml file:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.arthurvlug.captainhook.tutorial</groupId>
    <artifactId>helloworldservice</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>helloworldservice</name>

    <properties>
        <project.mainClass>com.arthurvlug.captainhook.tutorial.helloworldservice.ServiceMain</project.mainClass>
    </properties>

    <parent>
        <groupId>com.arthurvlug.captainhook</groupId>
        <artifactId>framework-core-server</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}-clientlib</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</project>
```

The service can be started similarly to how you can start a Spring Boot application.
The main method of your ServiceMain class should call the run method on the Controller class.
That method starts up the service.

The parameters are a ServerProperties (which will be generated when you first build the application), followed by a list of plugins (however, this is optional).
Lastly, it will also receive an array of String arguments, which can be used to configure plugins or internals of the framework.

```bash
mkdir -p $captainHookProject/captainhook/tutorial/helloWorldService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice
touch $captainHookProject/captainhook/tutorial/helloWorldService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/ServiceMain.java
```

```java
package com.arthurvlug.captainhook.tutorial.helloworldservice;

import com.arthurvlug.captainhook.tutorial.helloworldservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;

public class ServiceMain {
    public static void main(String[] args) {
        Controller.run(
                new ServerProperties(),
                args);
    }
}
```

Now create an activity class that handles the request. 

```bash
mkdir -p $captainHookProject/captainhook/tutorial/helloWorldService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/helloworld/
touch $captainHookProject/captainhook/tutorial/helloWorldService/src/main/java/com/arthurvlug/captainhook/tutorial/helloworldservice/server/activity/helloworld/HelloWorldActivity.java
```

```java
package com.arthurvlug.captainhook.tutorial.helloworldservice.server.activity.helloworld;

import com.arthurvlug.captainhook.tutorial.helloworldservice.activity.helloworld.HelloWorldInput;
import com.arthurvlug.captainhook.tutorial.helloworldservice.activity.helloworld.HelloWorldOutput;
import com.arthurvlug.captainhook.framework.server.SimpleActivity;
import com.arthurvlug.captainhook.framework.server.Activity;
import org.springframework.stereotype.Component;
import rx.Observable;

import java.time.Instant;

@Activity
@Component
public class HelloWorldActivity extends SimpleActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public Observable<HelloWorldOutput> enact(HelloWorldInput helloWorldInput) {
        final HelloWorldOutput output = HelloWorldOutput.builder()
                .message("Hello world!")
                .respondingTime(Instant.now())
                .build();

        return Observable.just(output);
    }
}
```

### Step 4: Build & run the application
To generate the classes are necessary, you should simply run in both projects:
```bash
cd $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib
mvn clean compile install
cd $captainHookProject/captainhook/tutorial/helloWorldService
mvn clean compile install
```

To run the service, run:
```bash
cd $captainHookProject/captainhook/tutorial/helloWorldService
mvn -Prun exec:java
```

Now you have a running service!
When you go to http://localhost:8080/ , it should say: "The server is online!".

Other services can can now consume it's clientlib and call it without much effort.
If those other services also implement the same model, their consumers can also easily call them.
Let's do that!

### Step 5: Create a second Captain Hook service
In this last step we're going to make a service that calls the service that we have created previously.

```bash 
cp -r $captainHookProject/captainhook/tutorial/helloWorldService $captainHookProject/captainhook/tutorial/helloMoonService
cp -r $captainHookProject/captainhook/tutorial/helloWorldServiceClientLib $captainHookProject/captainhook/tutorial/helloMoonServiceClientLib

perl -p -i -e 's/helloworldservice/hellomoonservice/g' `find $captainHookProject/captainhook/tutorial/helloMoonService/ -name *.*`



```
