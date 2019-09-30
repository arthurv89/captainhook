package com.arthurvlug.captainhook.framework.generation;

import com.arthurvlug.captainhook.framework.generation.clientlib.ActivityScanner;
import com.arthurvlug.captainhook.framework.generation.clientlib.ClientScanner;
import com.arthurvlug.captainhook.framework.generation.clientlib.GenerateClientLibClasses;
import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import static com.arthurvlug.captainhook.framework.generation.server.GenerateServerClasses.TEMPLATE_ENDPOINT;

public abstract class Generator {
    protected static final String TEMPLATE_BASE_PACKAGE = "com.arthurvlug";
    protected static final String TEMPLATE_SERVICE_NAME = "_service";
    protected static final String TEMPLATE_ENDPOINT = "_Endpoint";

    protected String doReplace(final String contents, final String from, final String to) {
        final String replacedString = contents.replace(from, to);
        if(contents.equals(replacedString)) {
            throw new RuntimeException(String.format("Nothing changed!\n\nFrom:\n%s\n\nTo:\n%s", from, to));
        }
        return replacedString;
    }


    private String templatePackageDirectory() {
        return getPackage(TEMPLATE_BASE_PACKAGE, TEMPLATE_SERVICE_NAME).replace(".", "/");
    }

    protected String getPackage(final String basePackage, final String serviceName) {
        return String.format("%s.%s", basePackage, serviceName);
    }

    @AllArgsConstructor
    public class EntryConfig {
        private final String basePackage;
        private final String serviceName;
        public final String endpointName;

        public String getPackage() {
            return Generator.this.getPackage(basePackage, serviceName);
        }
    }


    protected static String outputClass(final EntryConfig c) {
        return String.format("%s.activity.%s.%sOutput", c.getPackage(), c.endpointName.toLowerCase(), c.endpointName);
    }

    protected static String inputClass(final EntryConfig c) {
        return String.format("%s.activity.%s.%sInput", c.getPackage(), c.endpointName.toLowerCase(), c.endpointName);
    }

    protected String lowerFirst(final String s) {
        char[] c = s.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    private void prepareFolders(final File from, final File temp, final String basePackage, final String serviceName, final String name) throws IOException {
        FileUtils.deleteDirectory(temp);

        // Create base/package (e.g. target/dependency-resources/framework-core/generated/nl/arthurvlug/captainhook/exampleservice)
        final File newTarget = new File(temp, String.format("%s/%s", basePackage.replace(".", "/"), serviceName));
        FileUtils.deleteDirectory(newTarget);
        FileUtils.forceMkdir(newTarget);

        final File _serviceFolder = new File(from, templatePackageDirectory());
        File serverFolder = new File(_serviceFolder, name);
        File newServerFolder = new File(newTarget, name);
        if(!serverFolder.exists()) {
            final boolean made = serverFolder.mkdirs();
            if(!made) {
                throw new RuntimeException("Could not create dir");
            }
        }
        FileUtils.copyDirectory(serverFolder, newServerFolder);
    }



    private void replaceFileContents(final String basePackage, final String serviceName, final File temp, final Properties properties) throws IOException {
        final Set<String> activities = ActivityScanner.run(String.format("%s.%s", basePackage, serviceName));
        final List<String> clientClassPackages = ClientScanner.run(String.format("%s.%s", basePackage, serviceName))
                .stream()
                .map(c -> String.format("%s.class", c.getName()))
                .collect(Collectors.toList());

        Files.walk(Paths.get(temp.toURI()))
                .parallel()
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        final File file = path.toFile();
                        final String contents = FileUtils.readFileToString(file, Charset.defaultCharset());

                        final String newContents = replace(contents, basePackage, serviceName, activities, properties);
                        FileUtils.write(file, newContents, Charset.defaultCharset());
                    } catch (IOException e) {
                        Throwables.propagate(e);
                    }
                });
    }

    protected void run(final String serviceName, final String basePackage, final String folderName) throws IOException {
        final String workingDirectory = new File("").getAbsolutePath();

        final File from = new File(workingDirectory, "/target/dependency-resources/framework-core/generated-template");
        final File temp = new File(workingDirectory, "/target/dependency-resources/framework-core/generated");

        final Properties properties = new Properties();
        properties.load(GenerateClientLibClasses.class.getClassLoader().getResourceAsStream("application.properties"));

        prepareFolders(from, temp, basePackage, serviceName, folderName);
        replaceFileContents(basePackage, serviceName, temp, properties);

        final File to = new File(workingDirectory, "/src/main/generated-sources");
        FileUtils.deleteDirectory(to);
        FileUtils.moveDirectory(temp, to);
    }

    protected EntryConfig createTemplateEntryConfig() {
        return new EntryConfig(
                TEMPLATE_BASE_PACKAGE,
                TEMPLATE_SERVICE_NAME,
                TEMPLATE_ENDPOINT
        );
    }

    protected List<EntryConfig> getEntryConfigs(final String basePackage, final String serviceName, final Set<String> activities) {
        return activities.stream()
                .map(activity -> new EntryConfig(basePackage, serviceName, activity))
                .collect(Collectors.toList());
    }

    abstract protected String replace(final String contents, final String basePackage, final String serviceName, final Set<String> activities, final Properties properties);
}
