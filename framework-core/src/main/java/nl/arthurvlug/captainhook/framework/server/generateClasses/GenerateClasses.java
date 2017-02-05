package nl.arthurvlug.captainhook.framework.server.generateClasses;

import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateClasses {
    public static void main(String[] args) throws IOException {
        final String base = new File("").getAbsolutePath();
        final File FROM = new File(base, "/src/main/generated-base");
        final File TO = new File(base, "/src/main/generated");
        FileUtils.copyDirectory(FROM, TO);
        final String serviceName = args[0].replace("-clientlib", "");

        final Set<String> activities = ActivityScanner.run(serviceName);

        final Stream<Path> fileTemplates = Files.walk(Paths.get(TO.toURI()))
                .filter(Files::isRegularFile);
        fileTemplates.forEach(path -> {
            try {
                final File file = path.toFile();
                final String contents = FileUtils.readFileToString(file, Charset.defaultCharset());
                final String newContents = replace(contents, serviceName, activities);
                FileUtils.write(file, newContents, Charset.defaultCharset());
                rename(file, serviceName);
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        });
    }

    private static void rename(final File file, final String serviceName) {
        File newFile = new File(file.getParentFile(), upperFirst(serviceName) + file.getName());
        file.renameTo(newFile);
    }

    private static String replace(final String contents, final String serviceName, final Set<String> activities) {
        final EntryConfig entryPrototype = new EntryConfig("[Endpoint]", "[serviceName]");
        final List<EntryConfig> endpointConfig = activities.stream()
                .map(activity -> new EntryConfig(activity, serviceName))
                .collect(Collectors.toList());

        final String endpointDeclarations = endpointConfig.stream()
                .map(s -> endpoint(s))
                .collect(Collectors.joining("\n    "));

        final String entryDeclarations = endpointConfig.stream()
                .map(c -> entry(c))
                .collect(Collectors.joining("\n                "));


        final String serviceMethodDeclarations = endpointConfig.stream()
                .map(c -> serviceMethod(c))
                .collect(Collectors.joining("\n\n    "));


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
                .replace(serviceMethod(entryPrototype), serviceMethodDeclarations)
                .replace("[serviceName]", serviceName.toLowerCase())
                .replace("[ServiceName]", upperFirst(serviceName));
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

    private static String upperFirst(final String s) {
        char c[] = s.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }
}
