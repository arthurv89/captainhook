package nl.arthurvlug.captainhook.framework.generation.server;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import nl.arthurvlug.captainhook.framework.generation.clientlib.ActivityScanner;
import nl.arthurvlug.captainhook.framework.generation.clientlib.ClientScanner;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateServerClasses {
    public static final String TEMPLATE_BASE_PACKAGE = "com.arthurvlug";
    public static final String TEMPLATE_SERVICE_NAME = "_service";

    public static void main(String[] args) throws IOException {
        final String serviceName = args[0];
        final String basePackage = args[1];

        final String workingDirectory = new File("").getAbsolutePath();

        final File from = new File(workingDirectory, "/target/dependency-resources/framework-core/generated-template");
        final File temp = new File(workingDirectory, "/target/dependency-resources/framework-core/generated");

        prepareFolders(from, temp, basePackage, serviceName);
        replaceFiles(basePackage, serviceName, temp);

        final File to = new File(workingDirectory, "/src/main/generated-sources");
        FileUtils.deleteDirectory(to);
        FileUtils.moveDirectory(temp, to);
    }

    private static void prepareFolders(final File from, final File temp, final String basePackage, final String serviceName) throws IOException {
        FileUtils.deleteDirectory(temp);

        // Create base/package (e.g. target/dependency-resources/framework-core/generated/nl/arthurvlug/captainhook/exampleservice)
        final File newTarget = new File(temp, basePackage.replace(".", "/") + "/" + serviceName);
        FileUtils.deleteDirectory(newTarget);
        FileUtils.forceMkdir(newTarget);

        final File _serviceFolder = new File(from, packageDirectory());
        FileUtils.copyDirectory(new File(_serviceFolder, "server"), new File(newTarget, "server"));
    }


    private static String getPackage(final String basePackage, final String serviceName) {
        return basePackage + "." + serviceName;
    }

    private static String packageDirectory() {
        return getPackage(TEMPLATE_BASE_PACKAGE, TEMPLATE_SERVICE_NAME).replace(".", "/");
    }



    private static void replaceFiles(final String basePackage, final String serviceName, final File temp) throws IOException {
        final Set<String> activities = ActivityScanner.run(basePackage + "." + serviceName);
        final List<String> clientClassPackages = ClientScanner.run(basePackage + "." + serviceName)
                .stream()
                .map(c -> c.getName() + ".class")
                .collect(Collectors.toList());

        Files.walk(Paths.get(temp.toURI()))
                .parallel()
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        final File file = path.toFile();
                        final String contents = FileUtils.readFileToString(file, Charset.defaultCharset());
                        final String newContents = replace(contents, basePackage, serviceName, activities, clientClassPackages);
                        FileUtils.write(file, newContents, Charset.defaultCharset());
                    } catch (IOException e) {
                        Throwables.propagate(e);
                    }
                });
    }

    private static String replace(final String contents, final String basePackage, final String serviceName, final Set<String> activities, final List<String> clientClassPackageList) {
        final EntryConfig entryPrototype = new EntryConfig(TEMPLATE_BASE_PACKAGE,
                                                           TEMPLATE_SERVICE_NAME,
                                                           "_Endpoint"
        );
        final List<EntryConfig> endpointConfigs = activities.stream()
                .map(activity -> new EntryConfig(basePackage, serviceName, activity))
                .collect(Collectors.toList());

        final String endpointDeclarations = endpointConfigs.stream()
                .map(s -> endpoint(s))
                .collect(Collectors.joining("\n    "));

        final String entryDeclarations = endpointConfigs.stream()
                .map(c -> entry(c))
                .collect(Collectors.joining("\n                "));


        final String serviceMethodDeclarations = endpointConfigs.stream()
                .map(c -> serviceMethod(c))
                .collect(Collectors.joining("\n\n    "));

        String clientClassesString = Joiner.on(",\n        ").join(clientClassPackageList);

        final String prototypeEndpoint = endpoint(entryPrototype);
        final String prototypeEntry = entry(entryPrototype);
        final String prototypeServiceMethod = serviceMethod(entryPrototype);
        final String prototypePackage = entryPrototype.getPackage();
        final String prototypeClientClasses = "nl.arthurvlug.captainhook.framework.server.AbstractClientSpringComponents.class";

        final String replace1 = contents.replace(prototypeEndpoint, endpointDeclarations);
        final String replace2 = replace1.replace(prototypeEntry, entryDeclarations);
        final String replace3 = replace2.replace(prototypeServiceMethod, serviceMethodDeclarations);
        final String replace4 = replace3.replace(prototypeClientClasses, clientClassesString);

        final String replace5 = replace4.replace(prototypePackage, getPackage(basePackage, serviceName));
        final String replace7 = replace5.replace("_Endpoint", serviceName);
        return replace7;
    }

    @AllArgsConstructor
    static class EntryConfig {
        private final String basePackage;
        private final String serviceName;
        private final String endpointName;

        private String getPackage() {
            return GenerateServerClasses.getPackage(basePackage, serviceName);
        }
    }


    private static String endpoint(final EntryConfig c) {
        return "public static final String " + c.endpointName + "Endpoint = \"" + c.endpointName + "\";";
    }

    private static String entry(final EntryConfig c) {
        return ".put(" + c.endpointName + "Endpoint, new IOType<>(new TypeToken<Request<" + inputClass(c) + ">>() {}, new TypeToken<Response<" + outputClass(c) + ">>() {}))";
    }

    private static String serviceMethod(final EntryConfig c) {
        return "public Observable<" + outputClass(c) + "> " + lowerFirst(c.endpointName) + "Call(final " + inputClass(c) + " input) {\n" +
                "        return createCall(ActivityConfiguration." + c.endpointName + "Endpoint, input);\n" +
                "    }";
    }

    private static String outputClass(final EntryConfig c) {
        return c.getPackage() + ".activity." + c.endpointName.toLowerCase() + "." + c.endpointName + "Output";
    }

    private static String inputClass(final EntryConfig c) {
        return c.getPackage() + ".activity." + c.endpointName.toLowerCase() + "." + c.endpointName + "Input";
    }

    private static String lowerFirst(final String s) {
        char c[] = s.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }
}
