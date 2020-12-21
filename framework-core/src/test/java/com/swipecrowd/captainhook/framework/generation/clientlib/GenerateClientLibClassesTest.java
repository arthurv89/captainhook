package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GenerateClientLibClassesTest {
    private static final String URL = "/Users/arthur/workspace/captainhook-all/captainhook/framework-core-clientlib";
    private static final String serviceName = "helloworldservice-clientlib";
    private static final String basePackage = "com.swipecrowd.captainhook.tutorial";
    public static final String CLIENT_PACKAGE_PATH = "com/swipecrowd/captainhook/tutorial/helloworldservice/client/";
    public static final String EMPTY_FOLDER = "empty/";

    @Test
    public void testJavaClient() throws Exception {
        final String fileName = "JavaClient.java";
        compare(fileName, EMPTY_FOLDER);
    }

    @Test
    public void testJavascriptClient() throws Exception {
        final String fileName = "JavascriptClient.js";
        compare(fileName, EMPTY_FOLDER);
    }

    private void compare(String fileName, String folder) throws Exception {
        final String[] args = new String[] {serviceName, basePackage};

        GenerateClientLibClasses.main(args);

        final File generatedFile = generatedFile(CLIENT_PACKAGE_PATH, fileName);
        final List<String> generated = FileUtils.readLines(generatedFile, StandardCharsets.UTF_8);
        final String generatedString = cleanUpLines(generated);

        final InputStream resourceAsStream = GenerateClientLibClassesTest.class.getClassLoader().getResourceAsStream(folder + fileName);
        final List<String> lines = IOUtils.readLines(resourceAsStream, StandardCharsets.UTF_8);
        final String expectedString = cleanUpLines(lines);

        assertThat(generatedString).isEqualTo(expectedString);
    }

    private File generatedFile(String packagePath, String fileName) {
        return new File(URL + "/src/main/generated-sources/" + packagePath + fileName);
    }

    private String cleanUpLines(List<String> generated) {
        return Joiner.on("\n").join(generated.stream().map(x -> x.trim()).collect(Collectors.toList()));
    }
}
