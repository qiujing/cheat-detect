package edu.hrbust.cheatDetect;

import edu.hrbust.cheatDetect.bean.R;
import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.parser.ParserLanguage;
import org.eclipse.cdt.internal.core.dom.parser.c.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class MethodAST {
    private static final Logger logger = Logger.getLogger(MethodAST.class);
    private static final AtomicInteger atomic = new AtomicInteger(0);

    private final static int NOT_PRINT = 0;
    private final static int DIRCT_PRINT = 1;
    private final static int NOT_DIRECT_PRINT = 2;
    private static final Set<String> C_OUTPUT_FUNCTION_LIST = new HashSet<>(Arrays.asList("fputc", "fputs", "printf", "puts", "putchar", "putc"));
    private static final Set<String> C_INPUT_FUNCTION_LIST =
            new HashSet<>(Arrays.asList("fopen", "fread", "fscanf", "scanf", "fgetc", "fgets", "getc", "getchar", "gets",
                    "__flsbuf"));

    private static boolean isConstant(IASTNode node) {
        if (node instanceof CASTLiteralExpression) {
            return true;
        }

        if (node instanceof CASTUnaryExpression) {
            return node.getChildren()[0] instanceof CASTLiteralExpression;
        }

        return false;
    }

    /// <summary>
    /// is direct output?
    /// </summary>
    /// <param name="node"></param>
    /// <returns>0: not a output call 1: it is a direct output call 2: it is not a direct output call</returns>
    private static int isDirectPrint(CASTFunctionCallExpression node) {
        String fname = node.getFunctionNameExpression()
                .getRawSignature();

        if (C_OUTPUT_FUNCTION_LIST.contains(fname)) {
            IASTNode[] nodes = node.getChildren();
            int c = 0;
            for (IASTNode n : nodes) {
                if (isConstant(n)) {
                    c++;
                }
            }

            return c == nodes.length - 1 ? DIRCT_PRINT : NOT_DIRECT_PRINT;
        }

        return NOT_PRINT;
    }

    private static boolean isSuspect(IASTTranslationUnit tu) {
        boolean bStop = false;
        boolean ret = false;
        Stack<IASTNode> work_list = new Stack<>();
        work_list.push(tu);
        while (work_list.size() > 0 && !bStop) {
            IASTNode node = work_list.pop();
            if (node instanceof CASTFunctionCallExpression) {
                CASTFunctionCallExpression expression = (CASTFunctionCallExpression) node;
                int r = isDirectPrint(expression);
                switch (r) {
                    case DIRCT_PRINT:
                        ret = true;
                        break;
                    case NOT_DIRECT_PRINT:
                        bStop = true;
                        break;
                }
            } else if (node instanceof CASTProblemExpression || node instanceof CASTProblem || node instanceof CASTProblemStatement || node instanceof
                    CASTProblemDeclaration) {
                bStop = true;
            } else {
                for (IASTNode n : node.getChildren()) {
                    work_list.push(n);
                }
            }
        }

        return ret && !bStop;
    }

    /// <summary>
    /// Detect
    /// int main()
    /// {
    ///     printf("%d", 12);
    ///     return 0;
    /// }
    /// </summary>
    /// <param name="tu"></param>
    /// <returns></returns>
    private static boolean isCheat(IASTTranslationUnit tu) {
        boolean bStop = false;
        boolean ret = false;
        Stack<IASTNode> work_list = new Stack<>();
        work_list.push(tu);
        while (work_list.size() > 0 && !bStop) {
            IASTNode node = work_list.pop();
            if (node instanceof CASTFunctionCallExpression) {
                CASTFunctionCallExpression exp = (CASTFunctionCallExpression) node;
                String fname = exp.getFunctionNameExpression()
                        .getRawSignature();

                if (C_OUTPUT_FUNCTION_LIST.contains(fname)) {
                    IASTNode[] nodes = node.getChildren();
                    int c = 0;
                    for (IASTNode n : nodes) {
                        if (isConstant(n)) {
                            c++;
                        }
                    }

                    if (c == nodes.length - 1) // found?
                    {
                        if (node.getParent().getParent().getParent() instanceof CASTFunctionDefinition) // at the top level?
                        {
                            ret = true;
                        } else {
                            bStop = true;
                        }
                    } else {
                        bStop = true;
                    }
                }
            } else if (node instanceof CASTProblemExpression || node instanceof CASTProblem || node instanceof CASTProblemStatement || node instanceof
                    CASTProblemDeclaration) {
                bStop = true;
            } else {
                for (IASTNode n : node.getChildren()) {
                    work_list.push(n);
                }
            }
        }

        return ret && !bStop;
    }

    /// <summary>
    /// Detect 
    /// int main()
    /// {
    ///     int f = 1;
    ///     printf("%d", f);
    /// }
    /// </summary>
    /// <param name="tu"></param>
    /// <returns></returns>
    private static boolean isCheat2(IASTTranslationUnit tu) {
        boolean hasOutput = false;
        for (IASTNode node : tu.getChildren()) {
            if (node instanceof CASTFunctionDefinition) {
                for (IASTNode node1 : node.getChildren()) {
                    if (node1 instanceof CASTCompoundStatement) {
                        for (IASTNode node2 : node1.getChildren()) {
                            if (node2 instanceof CASTExpressionStatement) {
                                for (IASTNode node3 : node2.getChildren()) {
                                    if (node3 instanceof CASTFunctionCallExpression) {
                                        CASTFunctionCallExpression exp =
                                                (CASTFunctionCallExpression) node3;
                                        String fname = exp.getFunctionNameExpression()
                                                .getRawSignature();

                                        // 1. Check whether it is a output call
                                        if (C_OUTPUT_FUNCTION_LIST.contains(fname)) {
                                            hasOutput = true;
                                        } else {
                                            return false;
                                        }

                                        // 2. Check whether all agruments are constants or variables
                                        IASTNode[] node4 = node3.getChildren();
                                        for (IASTNode n : node4) {
                                            if (!(n instanceof CASTIdExpression) && !isConstant(n)) {
                                                return false;
                                            }
                                        }
                                    } else if (node3 instanceof CASTBinaryExpression) {
                                        if (!isConstant(node3.getChildren()[1])) {
                                            return false;
                                        }
                                    } else {
                                        return false;
                                    }
                                }
                            } else if (node2 instanceof CASTDeclarationStatement) {
                                IASTNode n1 = node2.getChildren()[0];
                                if (n1 instanceof CASTSimpleDeclaration) {
                                    IASTNode[] n2 = n1.getChildren();
                                    if (n2.length > 1) {
                                        IASTNode n3 = n2[1];
                                        if (n3 instanceof CASTDeclarator) {
                                            IASTNode[] n4 = n3.getChildren();
                                            if (n4.length > 1) {
                                                IASTNode n5 = n4[1];
                                                if (n5 instanceof CASTEqualsInitializer) {
                                                    IASTNode[] n6 = n5.getChildren();
                                                    if (!isConstant(n6[0])) {
                                                        return false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (node2 instanceof CASTReturnStatement || node2 instanceof CASTNullStatement) {
                                // do nothing
                            } else {
                                return false;
                            }
                        }
                    }
                }
            } else {
                // return false;
            }
        }
        return hasOutput;
    }

    /// <summary>
    /// 是否调用输入函数
    /// </summary>
    /// <param name="tu"></param>
    /// <returns>是否调用输入函数</returns>
    public static boolean hasInputCall(IASTTranslationUnit tu) {
        Stack<IASTNode> work_list = new Stack<>();
        work_list.push(tu);
        while (work_list.size() > 0) {
            IASTNode node = work_list.pop();
            if (node instanceof CASTFunctionCallExpression) {
                CASTFunctionCallExpression expression = (CASTFunctionCallExpression) node;
                String fname = expression.getFunctionNameExpression()
                        .getRawSignature();

                boolean bStop = C_INPUT_FUNCTION_LIST.contains(fname);
                if (bStop) {
                    return true;
                }
            } else if (node instanceof CASTProblemExpression || node instanceof CASTProblem || node instanceof CASTProblemStatement || node instanceof
                    CASTProblemDeclaration) {
                return true;
            } else {
                for (IASTNode n : node.getChildren()) {
                    work_list.push(n);
                }
            }
        }

        return false;
    }

    static void dump(IASTNode node, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("....");
        }
        String name = node.getClass().getName();
        int p = name.lastIndexOf('.');
        System.out.println(name.substring(p + 1));

        IASTNode[] nodes = node.getChildren();
        for (IASTNode n : nodes) {
            dump(n, level + 1);
        }
    }

    public static void main(String[] args) {
        // prepare result file
        File resultFile = new File("d:\\ASTR.txt");
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
            IASTTranslationUnit tu = CppParser.parse(f.getAbsolutePath(), ParserLanguage.C, false);
            int state = 0;
            if (isCheat(tu)) {
                state = 1;
            } else if (isCheat2(tu)) {
                state = 2;
            }

            // Report progress
            int done = atomic.incrementAndGet();
            pb.stepTo(done);

            return new R(f.getName(), state == 0 ? -1 : 1.0);
        }).filter(r -> r.similarity > 0).forEach(r -> {
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
