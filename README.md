# Captain Hook

## Introduction

Captain Hook is an API framework that makes sure the client and the server use the same contract to communicate.
The server generates helper classes so clients can use that to do calls to the server without any configuration.
Because the server knows the domain model, the helper classes not only take care of calling the server but also take care of serializing/deserializing the objects so the client doesn't have to do that.


## Project structure and responabilities

For each service we need 2 modules: a "server" and a "clientlib".

### The server
The server module implements all the necessary funcionalities for that service.
In order to handle requests, you need to define an Activity class.

Activities are similar to Spring Controllers and simply do business logic and return a response.
An Activity only handles one type of request.
It receives a single parameter (which can be a complex object by itself) and returns a simple response (wrapped in an Observable type).

### The client
The client module is the module that gets shared with consuming services.
This is the reason it has to be split from the service' business logic: this way it's way smaller and also we don't expose anything that consumers shouldn't know about.

The module should expose a Client class and the service's domain classes.
These classes are enough to make type-checked calls to a server without needing any configurations on the consumer side (apart from adding the clientlib as a dependency in the project).

## How to get started
It's easiest to start with the clientlib module.

### Step 1: Create a clientlib module:
In this document we are going to create a service called ExampleService

### Add parent in pom.xml
First, create a clientlib project and add the following parent:
```xml
<parent>
    <groupId>com.arthurvlug.captainhook</groupId>
    <artifactId>framework-core-clientlib</artifactId>
    <version>1.0-SNAPSHOT</version>
</parent>
```

### Add Input and output classes

The Activity input class can be anything you want, as long as it inherits from the Input class.

In the example below, we used Lombok to add getters and a builder to generate this object in a clean way, and to get it's parameters without creating the getter methods ourselves.

```java
package com.arthurvlug.captainhook.exampleservice.activity.helloworld;

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
 
```java
package com.arthurvlug.captainhook.exampleservice.activity.helloworld;

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
```
name = Example Service
server.port = 8080
```

With the clientlib in place, we can now move on to the server module.


### Step 2: Create a server module:

Again, after creation of the server module, let's change the pom file:
```xml
    <properties>
        <project.mainClass>com.arthurvlug.captainhook.exampleservice.ServiceMain</project.mainClass>
    </properties>

    <parent>
        <groupId>com.arthurvlug.captainhook</groupId>
        <artifactId>framework-core-server</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.1.8.RELEASE</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>${project.artifactId}-clientlib</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

The service can be started similarly to how you can start a Spring Boot application.
The main method of your ServiceMain class should call the run method on the Controller class.
That method starts up the service.

The parameters are a ServerProperties (which will be generated when you first build the application), followed by a list of plugins (however, this is optional).
Lastly, it will also receive an array of String arguments, which can be used to configure plugins or internals of the framework.

```
package com.arthurvlug.captainhook.exampleservice;

import com.arthurvlug.captainhook.exampleservice.server.SelfDiagnose;
import com.arthurvlug.captainhook.exampleservice.server.ServerProperties;
import com.arthurvlug.captainhook.framework.server.Controller;
import com.arthurvlug.captainhook.framework.server.plugins.selfdiagnose.SelfDiagnosePlugin;

public class ServiceMain {
    public static void main(String[] args) {
        Controller.run(
                new ServerProperties(),
                args);
    }
}

```

Now create an activity class that handles the request. 
```
package com.arthurvlug.captainhook.exampleservice.server.activity.helloworld;

import com.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldInput;
import com.arthurvlug.captainhook.exampleservice.activity.helloworld.HelloWorldOutput;
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
mvn clean compile install
```

To run the service, run:
```bash
cd exampleService
mvn -Prun exec:java
```

Now you have a running service!
Other services can can now consume it's clientlib and call it without much effort.
If those other services also implement the same model, their consumers can also easily call them.