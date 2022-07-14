package edu.hrbust.cheatDetect;

import edu.hrbust.cheatDetect.bean.R;
import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Line Stat
 */
public class MethodLineStat {
    private static Logger logger = Logger.getLogger(MethodLineStat.class);
    private static AtomicInteger atomic = new AtomicInteger(0);

    public static void main(String[] args) {        
        File resultFile = new File("d:\\lineStatR.txt");
        if (resultFile.exists()) {
            FileUtils.deleteQuietly(resultFile);
        }

        File[] files = new File("D:\\cheat-detect-source").listFiles();
        if (files == null) {
            logger.info("No file to search");
            return;
        }

        ProgressBar pb = new ProgressBar("Detect", files.length);

        Arrays.stream(files).parallel().map(f -> {
            String content;

            // read file
            try {
                content = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.error(e);
                return new R(null, 0.0);
            }

            // Remove comments
            content = content.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "");
            String[] lines = content.split("[\\r\\n]+");

            List<Integer> arr = Arrays.stream(lines).map(String::trim).filter(line -> {
                if (line.length() > 0) {
                    char ch = line.charAt(0);
                    if (ch == '#' || ch == '{' || ch == '}') {
                        return false;
                    }

                    if (line.startsWith("return") || line.startsWith("int main")) {
                        return false;
                    }

                    return true;
                }
                return false;
            }).map(line -> {
                if (line.startsWith("printf(") || line.startsWith("puts(") || line.startsWith("putchar(") || line.startsWith("putc(")) {
                    return 1;
                }
                return 0;
            }).collect(Collectors.toList());

            int hit = 0;
            for (int i : arr) {
                if (i == 1) {
                    hit++;
                }
            }

            // Report progress
            int done = atomic.incrementAndGet();
            pb.stepTo(done);

            return new R(f.getName(), (double) hit / arr.size());
        })
                .filter(r -> r.similarity > 0.3).forEach(r -> {
                    try {
                        FileUtils.writeStringToFile(resultFile, r.fileName + " " + r.similarity + "\r\n", StandardCharsets.UTF_8, true);
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }
        );

        pb.close();

    }
}
