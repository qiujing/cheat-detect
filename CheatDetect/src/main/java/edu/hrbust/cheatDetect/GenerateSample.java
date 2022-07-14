package edu.hrbust.cheatDetect;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GenerateSample {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 20; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("#include <stdio.h>\r\nint main(){");
            for (int j = 0; j < i; j++) {
                sb.append("printf(\"123\");");
            }
            sb.append("}");

            FileUtils.writeStringToFile(new File("/home/qiujing/DOC/sample/ss" + i + ".c"), sb.toString(), StandardCharsets.UTF_8);
        }
    }
}
