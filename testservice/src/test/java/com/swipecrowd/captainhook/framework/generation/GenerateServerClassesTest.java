package com.swipecrowd.captainhook.framework.generation;

import com.google.common.base.Joiner;
import com.swipecrowd.captainhook.framework.generation.server.GenerateServerClasses;
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

public class GenerateServerClassesTest {
    private static final String URL = "/Users/arthur/workspace/captainhook-all/captainhook/testservice";
    private static final String serviceName = "testservice";
    private static final String basePackage = "com.swipecrowd.captainhook.tutorial";
    public static final String SERVER_PACKAGE_PATH = "com/swipecrowd/captainhook/tutorial/testservice/server/";
    public static final String EMPTY_FOLDER = "standard/";

    @Test
    public void testServerProperties() throws Exception {
        final String fileName = "ServerProperties.java";
        runTest(fileName, EMPTY_FOLDER);
    }

    private void runTest(String fileName, String folder) throws Exception {
        final String[] args = new String[] {serviceName, basePackage};
        GenerateServerClasses.main(args);

        final String generatedString = getGeneratedFileContents(fileName);
        final String expectedString = getExpectedFileContents(fileName, folder);
        assertThat(generatedString).isEqualTo(expectedString);
    }

    private String getExpectedFileContents(String fileName, String folder) throws IOException {
        final InputStream resourceAsStream = GenerateServerClassesTest.class.getClassLoader().getResourceAsStream(folder + fileName);
        final List<String> lines = IOUtils.readLines(resourceAsStream, StandardCharsets.UTF_8);
        final String expectedString = cleanUpLines(lines);
        return expectedString;
    }

    private String getGeneratedFileContents(String fileName) throws IOException {
        final File generatedFile = generatedFile(fileName);
        final List<String> generated = FileUtils.readLines(generatedFile, StandardCharsets.UTF_8);
        final String generatedString = cleanUpLines(generated);
        return generatedString;
    }

    private File generatedFile(String fileName) {
        return new File(URL + "/src/main/generated-sources/" + SERVER_PACKAGE_PATH + fileName);
    }

    private String cleanUpLines(List<String> generated) {
        return Joiner.on("\n").join(generated.stream().map(x -> x.trim()).collect(Collectors.toList()));
    }
}
