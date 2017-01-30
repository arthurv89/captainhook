package nl.arthurvlug.captainhook.framework.server;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateClasses {

    public static final String BASE = "/home/arthur/workspace/Java/captainhook/example-service-clientlib";
    private static final File FROM = new File(BASE, "/src/main/generated-base");
    private static final File TO = new File(BASE, "/src/main/generated");

    public static void main(String[] args) throws IOException {
        FileUtils.copyDirectory(FROM, TO);

        Files.walk(Paths.get(TO.toURI()))
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        String contents = FileUtils.readFileToString(file.toFile(), Charset.defaultCharset());
                        String newContents = replace(contents);
                        FileUtils.write(file.toFile(), newContents, Charset.defaultCharset());
                    } catch (IOException e) {
                        Throwables.propagate(e);
                    }
                });
    }

    private static String replace(final String contents) {
        final EntryConfig entryPrototype = new EntryConfig("XXX", "exampleservice");
        final List<EntryConfig> endpointConfig = ImmutableList.of(new EntryConfig("HelloWorld", "exampleservice"));

        final String endpointDeclarations = endpointConfig.stream()
                .map(s -> endpoint(s))
                .collect(Collectors.joining("\n    "));

        final String entryDeclarations = endpointConfig.stream()
                .map(c -> entry(c))
                .collect(Collectors.joining("\n                "));


        final String serviceMethodDeclarations = endpointConfig.stream()
                .map(c -> serviceMethod(c))
                .collect(Collectors.joining("\n\n                "));


        final String importDeclarations = endpointConfig.stream()
                .map(c -> inputImport(c))
                .collect(Collectors.joining("\n"));

        final String outputDeclarations = endpointConfig.stream()
                .map(c -> outputImport(c))
                .collect(Collectors.joining("\n"));

        return contents
                .replace(inputImport(entryPrototype), importDeclarations)
                .replace(outputImport(entryPrototype), outputDeclarations)
                .replace(endpoint(entryPrototype), endpointDeclarations)
                .replace(entry(entryPrototype), entryDeclarations)
                .replace(serviceMethod(entryPrototype), serviceMethodDeclarations);
    }

    private static String inputImport(final EntryConfig c) {
        return "import nl.arthurvlug.captainhook." + c.serviceName + ".activity." + c.helloWorld.toLowerCase() + "." + c.helloWorld + "Input;";
    }

    private static String outputImport(final EntryConfig c) {
        return "import nl.arthurvlug.captainhook." + c.serviceName + ".activity." + c.helloWorld.toLowerCase() + "." + c.helloWorld + "Output;";
    }

    @AllArgsConstructor
    static class EntryConfig {
        private final String helloWorld;
        private final String serviceName;
    }


    private static String endpoint(final EntryConfig s) {
        return "public static final String " + s.helloWorld + "Endpoint = \"" + s.helloWorld + "\";";
    }

    private static String entry(final EntryConfig c) {
        return ".put(" + c.helloWorld + "Endpoint, new IOType<>(new TypeToken<Request<" + c.helloWorld + "Input>>() {}, new TypeToken<Response<" + c.helloWorld + "Output>>() {}))";
    }

    private static String serviceMethod(final EntryConfig c) {
        return "public Call<" + c.helloWorld + "Output> " + lowerFirst(c.helloWorld) + "Call(final " + c.helloWorld + "Input input) {\n" +
                "        return createCall(" + c.helloWorld + "Endpoint, input);\n" +
                "    }";
    }

    private static String lowerFirst(final String s) {
        char c[] = s.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }
}
