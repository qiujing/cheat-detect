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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    static AtomicInteger atomic = new AtomicInteger(0);

    public static void main(String[] args) {
       File resultFile = new File("/home/qiujing/cfg-o2.txt");
       if (resultFile.exists()) {
           FileUtils.deleteQuietly(resultFile);
       }

       logger.info("Prepare sample graphs");

       // samples
       List<Graph> graphDb =
               Arrays.stream(Objects.requireNonNull(new File("/home/qiujing/DOC/sample-gt").listFiles())).parallel().map(f -> {
                   CppCFGForLinux cfg = new CppCFGForLinux();
                   return cfg.GetCFG(f);
               }).filter(Objects::nonNull).collect(Collectors.toList());

       logger.info(String.format("%d graphs loaded. Start searching", graphDb.size()));

       File[] files = new File("/home/qiujing/DOC/records").listFiles();
       if (files == null) {
           logger.info("No file to search");
           return;
       }
       ProgressBar pb = new ProgressBar("Detect", files.length);

       Arrays.stream(Objects.requireNonNull(files)).parallel().map(f -> {
           CppCFGForLinux cfg = new CppCFGForLinux();
           Graph targetGraph = cfg.GetCFG(f);
           if (targetGraph == null) {
               return new R(null, 0.0);
           }

           double max = 0.0;
           for (Graph b : graphDb) {
               double sim = CppCFGForLinux.similarity(targetGraph, b);
               if (sim > max) {
                   max = sim;
               }
               if (max > 0.99) { // early exit
                   break;
               }
           }

           //Optional<Double> ret = targetGraphs.stream().flatMap(a -> graphDb.stream().map(b -> CppCFGForLinux.similarity(a, b))).max(Double::compareTo);

           // Report progress
           int done = atomic.incrementAndGet();
           pb.stepTo(done);

           return new R(f.getName(), max);
       }).filter(r -> r.similarity > 0.5)
               .forEach(r -> {
                   try {
                       FileUtils.writeStringToFile(resultFile, r.fileName + " " + r.similarity + "\r\n", StandardCharsets.UTF_8, true);
                   } catch (IOException e) {
                       logger.error(e);
                   }
               });

       pb.close();

        // FP.process(new File("/home/qiujing"), 0.9);
    }
}
