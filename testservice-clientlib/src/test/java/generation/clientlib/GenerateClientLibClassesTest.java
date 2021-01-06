package generation.clientlib;

import com.google.common.base.Joiner;
import com.swipecrowd.captainhook.framework.generate.client.GenerateClientLibClasses;
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
    private static final String clientLibName = "testservice-clientlib";
    private static final String servicePackage = "com.swipecrowd.captainhook.test.testservice";
    public static final String CLIENT_PACKAGE_PATH = "com/swipecrowd/captainhook/test/testservice/client/";
    public static final String STANDARD_FOLDER = "standard/";

    @Test
    public void testJavaClient() throws Exception {
        final String fileName = "TestServiceJavaClient.java";
        compare(fileName);
    }

    @Test
    public void testJavascriptClient() throws Exception {
        final String fileName = "TestServiceJavascriptClient.js";
        compare(fileName);
    }

    private void compare(String fileName) throws Exception {
        final String[] args = new String[] {servicePackage};

        GenerateClientLibClasses.main(args);

        final String generatedString = readGeneratedString(fileName);
        final String expectedString = readExpectedString(fileName, STANDARD_FOLDER);
        assertThat(generatedString).isEqualTo(expectedString);
    }

    private String readExpectedString(String fileName, String folder) throws IOException {
        String resName = folder + fileName;
        System.out.println("Reading expected file " + resName);
        final InputStream resourceAsStream = GenerateClientLibClassesTest.class.getClassLoader().getResourceAsStream(resName);
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
