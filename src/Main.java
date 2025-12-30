import AST.Program;
import Visitor.ProjectVisitor;
import grammer.lexer.ProjectLexer;
import grammer.lexer.ProjectParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import java.io.IOException;

import static org.antlr.v4.runtime.CharStreams.fromFileName;

public class Main {
    public static void main(String[] args) throws Exception {
        try{
            String path = "test1.txt";
            CharStream input =fromFileName(path);
            ProjectLexer  lexer = new ProjectLexer(input);
            CommonTokenStream token = new CommonTokenStream(lexer);
            ProjectParser parser = new ProjectParser(token);
            ParseTree tree= parser.program();
            ProjectVisitor visitor = new ProjectVisitor();
            Program program = (Program) visitor.visit(tree);
            System.out.println("=== AST ===");
            System.out.println(program);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
