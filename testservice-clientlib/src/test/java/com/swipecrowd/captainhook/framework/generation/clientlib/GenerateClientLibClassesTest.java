package com.swipecrowd.captainhook.framework.generation.clientlib;

import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GenerateClientLibClassesTest {
    private static final String URL = "/Users/arthur/workspace/captainhook-all/captainhook/testservice-clientlib";
    private static final String serviceName = "testservice-clientlib";
    private static final String basePackage = "com.swipecrowd.captainhook.tutorial";
    public static final String CLIENT_PACKAGE_PATH = "com/swipecrowd/captainhook/tutorial/testservice/client/";
    public static final String EMPTY_FOLDER = "standard/";

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

        final String generatedString = readGeneratedString(fileName);
        final String expectedString = readExpectedString(fileName, folder);
        assertThat(generatedString).isEqualTo(expectedString);
    }

    private String readExpectedString(String fileName, String folder) throws IOException {
        final InputStream resourceAsStream = GenerateClientLibClassesTest.class.getClassLoader().getResourceAsStream(folder + fileName);
        final List<String> lines = IOUtils.readLines(resourceAsStream, StandardCharsets.UTF_8);
        final String expectedString = cleanUpLines(lines);
        return expectedString;
    }

    private String readGeneratedString(String fileName) throws IOException {
        final File generatedFile = generatedFile(fileName);
        final List<String> generated = FileUtils.readLines(generatedFile, StandardCharsets.UTF_8);
        final String generatedString = cleanUpLines(generated);
        return generatedString;
    }

    private File generatedFile(String fileName) {
        return new File(URL + "/src/main/generated-sources/" + CLIENT_PACKAGE_PATH + fileName);
    }

    private String cleanUpLines(List<String> generated) {
        return Joiner.on("\n").join(generated.stream().map(x -> x.trim()).collect(Collectors.toList()));
    }
}
