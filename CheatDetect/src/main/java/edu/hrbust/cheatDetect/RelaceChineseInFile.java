package edu.hrbust.cheatDetect;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RelaceChineseInFile {
    public static void main(String[] args) throws IOException {
        File[] files = new File("E:\\DOC\\test-source").listFiles();
        assert files != null;
        for (File file : files) {
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            content = content.replaceAll("[\\u4e00-\\u9fa5\\uFE30-\\uFFA0]+", "ABC");
            FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
        }
    }
}
