# Captain Hook

Captain Hook is an API framework that makes sure the client and the server use the same contract to communicate.
The server generates helper classes so clients can use that to do calls to the server without any configuration.
Because the server knows the domain model, the helper classes not only take care of calling the server but also take care of serializing/deserializing the objects so the client doesn't have to do that.

## How to get started

### Step 1: Create a client:
```xml
        <dependency>
            <groupId>nl.arthurvlug.captainhook</groupId>
            <artifactId>service-clientlib</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

```java
@SpringBootApplication
@Import(ExampleServiceClientComponentcanner.class)
public class ClientMain {
    public static void main(final String[] args) {
        CaptainHookApplication.run(ClientMain.class, args);
    }
}
```

```java
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientRunner extends AbstractClientRunner {
    private static final Logger logger = LoggerFactory.getLogger(ClientRunner.class);

    @Autowired
    private ExampleServiceClient exampleService;

    @Override
    public void run() {
        while(true) {
            doHelloWorldCall();
        }
    }

    private void doHelloWorldCall() {
        try {
            final HelloWorldInput helloWorldInput = HelloWorldInput.builder()
                    .name("Jantje (" + new Date().getTime() + ")")
                    .build();
            final HelloWorldOutput output = exampleService.helloWorldCall(helloWorldInput).call();
            logger.info(output.getMessage());
        } catch (DependencyException e) {
            logger.error(e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
        }
    }
}
```

### Step 2: Create a client-lib
```xml
        <dependency>
            <groupId>nl.arthurvlug.captainhook</groupId>
            <artifactId>framework</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

```java
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceConfiguration {
    public static final String baseUrl = "http://localhost:8080";
    public static final String PACKAGE_NAME = "nl.arthurvlug.captainhook.exampleservice";
}
```

```java
@Builder
@Getter
public class HelloWorldInput extends Input {
    private final String name;
}
```

```java
@Value
public class HelloWorldOutput extends Output {
    private final String name;
}
```

(todo: in the future we'll generate this client-lib from xsd. Add documentation)

### Step 3: Create a service
```xml
        <dependency>
            <groupId>nl.arthurvlug.captainhook</groupId>
            <artifactId>service-clientlib</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

```java
public class ServiceMain {
    public static void main(String[] args) {
        ExampleServiceController.run(args);
    }
}
```

```java
@Activity
public class HelloWorldActivity extends DefaultAbstractActivity<HelloWorldInput, HelloWorldOutput> {
    @Override
    public HelloWorldOutput enact(HelloWorldInput helloWorldInput) throws Exception {
        if(Math.random() < 0.3) {
            throw new RuntimeException("Something went wrong");
        }
        Thread.sleep((long) (Math.random() * 1000));
        return new HelloWorldOutput("Hello, " + helloWorldInput.getName() + "!");
    }
}
```
