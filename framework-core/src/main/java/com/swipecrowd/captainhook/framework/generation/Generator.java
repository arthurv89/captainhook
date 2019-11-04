package com.swipecrowd.captainhook.framework.generation;

import com.google.common.base.Throwables;
import com.swipecrowd.captainhook.framework.generation.clientlib.ActivityScanner;
import com.swipecrowd.captainhook.framework.generation.clientlib.GenerateClientLibClasses;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

public abstract class Generator {
    protected static final String TEMPLATE_BASE_PACKAGE = "com.swipecrowd";
    public static final String TEMPLATE_SERVICE_NAME = "_service";
    public static final String TEMPLATE_ENDPOINT = "_Endpoint";



    private void prepareFolders(final File from, final File temp, final String basePackage, final String serviceName, final String name) throws IOException {
        FileUtils.deleteDirectory(temp);

        // Create base/package (e.g. target/dependency-resources/framework-core/generated/com/swipecrowd/captainhook/exampleservice)
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

    private String templatePackageDirectory() {
        return getPackage(TEMPLATE_BASE_PACKAGE, TEMPLATE_SERVICE_NAME).replace(".", "/");
    }

    public static String getPackage(final String basePackage, final String serviceName) {
        return String.format("%s.%s", basePackage, serviceName);
    }

    private void replaceFileContents(final String basePackage, final String serviceName, final File tempFolder, final Properties properties) throws IOException {
        final Set<String> activities = ActivityScanner.run(String.format("%s.%s", basePackage, serviceName));
        Files.walk(Paths.get(tempFolder.toURI()))
                .parallel()
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        final File file = path.toFile();
                        final String contents = FileUtils.readFileToString(file, Charset.defaultCharset());

                        System.out.println(file.getName());
                        final String newContents = ReplacerType.fromFileName(file.getName()).replace(contents, basePackage, serviceName, activities, properties);
                        FileUtils.write(file, newContents, Charset.defaultCharset());
                    } catch (IOException e) {
                        Throwables.propagate(e);
                    }
                });
    }

    protected void run(final String serviceName, final String basePackage, final String folderName) throws IOException {
        final String workingDirectory = new File("").getAbsolutePath();

        final File fromFolder = new File(workingDirectory, "/target/dependency-resources/framework-core/generated-template");
        final File tempFolder = new File(workingDirectory, "/target/dependency-resources/framework-core/generated");

        final Properties properties = new Properties();
        final URLClassLoader classLoader = (URLClassLoader) GenerateClientLibClasses.class.getClassLoader();
        final String propertiesFile = "application.properties";

        //        System.out.println(getClass().getClassLoader().getResources())
        System.out.println("Working dir " + workingDirectory);
        System.out.println("getResource " + classLoader.getResource(propertiesFile));
        System.out.println("URLs " + Arrays.asList(classLoader.getURLs()));
        System.out.println("classLoader.findResource(propertiesFile) " + classLoader.findResource(propertiesFile));

        final InputStream resourceAsStream = classLoader.getResourceAsStream(propertiesFile);
        properties.load(resourceAsStream);

        prepareFolders(fromFolder, tempFolder, basePackage, serviceName, folderName);
        replaceFileContents(basePackage, serviceName, tempFolder, properties);

        final File to = new File(workingDirectory, "/src/main/generated-sources");
        FileUtils.deleteDirectory(to);
        FileUtils.moveDirectory(tempFolder, to);
    }
}
