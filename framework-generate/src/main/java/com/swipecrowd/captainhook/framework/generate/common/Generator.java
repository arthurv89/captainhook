package com.swipecrowd.captainhook.framework.generate.common;

import com.google.common.base.Throwables;
import com.swipecrowd.captainhook.framework.generate.client.ClientActivityScanner;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureClassLoader;
import java.util.Properties;
import java.util.Set;

public abstract class Generator {
    protected static final String TEMPLATE_BASE_PACKAGE = "com.swipecrowd.service__";
    public static final String TEMPLATE_SERVICE_NAME = "service__";
    public static final String TEMPLATE_ENDPOINT = "Endpoint__";

    protected void run(final String servicePackage,
                       final String folderName,
                       final ReplacerType replacerType) throws IOException {
        System.out.println(servicePackage);
        final String workingDirectory = new File("").getAbsolutePath();

        final File fromFolder = new File(workingDirectory, "/target/dependency-resources/framework-core/generated-template");
        final File tempFolder = new File(workingDirectory, "/target/dependency-resources/framework-core/generated");

        final Properties properties = parsePropertiesFile(workingDirectory);
        final String serviceName = (String) properties.get("*.*.name");

        System.out.println("Service name: " + serviceName);
        System.out.println("Preparing folders");
        prepareFolders(fromFolder, tempFolder, servicePackage, folderName);

        System.out.println("Replace file contents");
        replaceFileContents(servicePackage, serviceName, tempFolder, replacerType);

        final File to = new File(workingDirectory, "/src/main/generated-sources");
        System.out.println("Copy over the results to " + to.getAbsolutePath());

        FileUtils.deleteDirectory(to);
        FileUtils.moveDirectory(tempFolder, to);
    }

    private Properties parsePropertiesFile(String workingDirectory) throws IOException {
        final Properties properties = new Properties();

        final SecureClassLoader classLoader = (SecureClassLoader) Generator.class.getClassLoader();
        final String propertiesFile = "application.properties";

        System.out.println("Working dir " + workingDirectory);
        System.out.println("getResource " + classLoader.getResource(propertiesFile));

        System.out.println("Loading properties");
        final InputStream resourceAsStream = classLoader.getResourceAsStream(propertiesFile);
        properties.load(resourceAsStream);
        return properties;
    }

    private void replaceFileContents(final String servicePackage,
                                     final String serviceName,
                                     final File tempFolder,
                                     final ReplacerType replacerType) throws IOException {
        final Set<String> activities = ClientActivityScanner.run(servicePackage);
        System.out.println();
        System.out.println("Temp folder: " + tempFolder);
        Files.walk(Paths.get(tempFolder.toURI()))
                .parallel()
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        final File file = path.toFile();
                        final String contents = FileUtils.readFileToString(file, Charset.defaultCharset());

                        System.out.println("Replace file " + file.getAbsolutePath());
                        Replacer replacer = replacerType.fromFileName(file.getName());
                        final String newContents = replacer.replace(contents, servicePackage, serviceName, activities);
                        FileUtils.write(file, newContents, Charset.defaultCharset());

                        File newName = replacer.createRenamedFile(serviceName, file);
                        file.renameTo(newName);
                    } catch (IOException e) {
                        Throwables.propagate(e);
                    }
                });
    }

    private void prepareFolders(final File from,
                                final File temp,
                                final String servicePackage,
                                final String name) throws IOException {
        FileUtils.deleteDirectory(temp);

        // Create base/package (e.g. target/dependency-resources/framework-core/generated/com/swipecrowd/captainhook/exampleservice)
        final File newTarget = new File(temp, String.format("%s", servicePackage.replace(".", "/")));
        FileUtils.deleteDirectory(newTarget);
        FileUtils.forceMkdir(newTarget);

        final File service__Folder = new File(from, templatePackageDirectory());
        File serverFolder = new File(service__Folder, name);
        File newServerFolder = new File(newTarget, name);
        if(!serverFolder.exists()) {
            final boolean made = serverFolder.mkdirs();
            if(!made) {
                throw new RuntimeException("Could not create dir");
            }
        }
        FileUtils.copyDirectory(serverFolder, newServerFolder);
    }

    private String templatePackageDirectory() {
        return TEMPLATE_BASE_PACKAGE.replace(".", "/");
    }
}
