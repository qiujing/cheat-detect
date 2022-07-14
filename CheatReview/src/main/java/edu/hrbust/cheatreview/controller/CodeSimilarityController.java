package edu.hrbust.cheatreview.controller;

import com.alibaba.fastjson.JSON;
import edu.hrbust.analysis.codeSimilarity.bean.Graph;
import edu.hrbust.analysis.codeSimilarity.cpp.CppCFGForLinux;
import edu.hrbust.cheatreview.bean.CodeSimilarityResult;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class CodeSimilarityController {
    @PostMapping("/api/code-similarity")
    public CodeSimilarityResult codeSimilarity(@RequestBody String[] code) {
        CppCFGForLinux cfg = new CppCFGForLinux();//("C:\\App_Data\\mingw32");
        String file1 = CppCFGForLinux.saveCode(code[0]);
        String file2 = CppCFGForLinux.saveCode(code[1]);
        if (file1 == null || file2 == null) {
            return null;
        }

        Graph g = cfg.GetCFG(new File(file1));
        Graph g2 = cfg.GetCFG(new File(file2));

        FileUtils.deleteQuietly(new File(file1));
        FileUtils.deleteQuietly(new File(file2));

        String[] json = new String[2];
        json[0] = JSON.toJSONString(g, true);
        json[1] = JSON.toJSONString(g2, true);
        CodeSimilarityResult r = new CodeSimilarityResult();
        r.setGraphJson(json);
        r.setSimilarity(CppCFGForLinux.similarity(g, g2));

        return r;
    }
}
