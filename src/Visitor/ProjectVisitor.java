package Visitor;

import AST.*;
import AST.CSSTerms.*;
import AST.JinjaExpressions.*;
import AST.Statements.*;
import grammer.lexer.ProjectParser;
import grammer.lexer.ProjectParserBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

public class ProjectVisitor extends ProjectParserBaseVisitor <ASTNode> {
    @Override
    public ASTNode visitProgramDef(ProjectParser.ProgramDefContext ctx) {
        List<Statement> statements = new ArrayList<>();

        for (ProjectParser.NodeContext nodeCtx : ctx.node()) {
            ASTNode node = visit(nodeCtx);

            if (node instanceof Statement stmt) {
                statements.add(stmt);
            }
        }

        return new Program(ctx.getStart().getLine(), statements);
    }

    @Override
    public ASTNode visitCssColor(ProjectParser.CssColorContext ctx) {
        String value = ctx.getText();
        int line = ctx.getStart().getLine();
        return new CSSColor(line, value);
    }

    @Override
    public ASTNode visitCssFunctionTerm(ProjectParser.CssFunctionTermContext ctx) {
        return visit(ctx.cssFunction());
    }
    @Override
    public ASTNode visitFunction(ProjectParser.FunctionContext ctx) {
        int line = ctx.getStart().getLine();

        String functionName = ctx.IDENTIFIER_CSS().getText();

        CSSValueList arguments = null;
        if (ctx.cssValueList() != null) {
            arguments = (CSSValueList) visit(ctx.cssValueList());
        }

        return new CSSFunctionTerm(line, functionName, arguments);
    }


    @Override
    public ASTNode visitCssHslaColor(ProjectParser.CssHslaColorContext ctx) {
        String value = ctx.getText();
        int line = ctx.getStart().getLine();
        return new CSSHslaColor(line, value);
    }

    @Override
    public ASTNode visitCssHslColor(ProjectParser.CssHslColorContext ctx) {
        String value = ctx.getText();
        int line = ctx.getStart().getLine();
        return new CSSHslColor(line, value);
    }

    @Override
    public ASTNode visitCssIdentifier(ProjectParser.CssIdentifierContext ctx) {
        String value = ctx.getText();
        int line = ctx.getStart().getLine();
        return new CSSIdentifier(line, value);
    }

    @Override
    public ASTNode visitCssMultipleValues(ProjectParser.CssMultipleValuesContext ctx) {
        List<CSSTerm> terms = new ArrayList<>();
        for (ProjectParser.CssTermContext termCtx : ctx.cssTerm()) {
            CSSTerm term = (CSSTerm) visit(termCtx);
            if (term != null) {
                terms.add(term);
            }
        }
        int line = ctx.getStart().getLine();;
        return new CSSMultipleValues(line, terms);
    }

    @Override
    public ASTNode visitCssNumberTerm(ProjectParser.CssNumberTermContext ctx) {
        Boolean negative = ctx.DASH() != null;
        double number = Double.parseDouble(ctx.NUMBER_CSS().getText());
        String unit = null;
        if (ctx.UNIT() != null) {
            unit = ctx.UNIT().getText();
        }
        int line = ctx.getStart().getLine();
        return new CSSNumberTerm(line, negative, number, unit);
    }

    @Override
    public ASTNode visitCssRgbaColor(ProjectParser.CssRgbaColorContext ctx) {
        int line = ctx.getStart().getLine();
        String value = ctx.getText();
        return new CSSRgbaColor(line, value);
    }

    @Override
    public ASTNode visitCssRgbColor(ProjectParser.CssRgbColorContext ctx) {
        int line = ctx.getStart().getLine();
        String value = ctx.getText();
        return new CSSRgbColor(line, value);
    }

    @Override
    public ASTNode visitCssSingleValue(ProjectParser.CssSingleValueContext ctx) {
        int line = ctx.getStart().getLine();
        CSSTerm term = (CSSTerm) visit(ctx.cssTerm());
        return new CSSSingleValue(line, term);
    }

    @Override
    public ASTNode visitCssString(ProjectParser.CssStringContext ctx) {
        int line = ctx.getStart().getLine();
        String value = ctx.STRING_CSS().getText();
        return new CSSString(line, value);
    }
    @Override
    public ASTNode visitValueList(ProjectParser.ValueListContext ctx) {
        List<CSSValue> values = new ArrayList<>();
        int line = ctx.getStart().getLine();
        for (ProjectParser.CssValueContext c : ctx.cssValue()) {
            CSSValue value = (CSSValue) visit(c);
            if (value != null) {
                values.add(value);
            }
        }
        return new CSSValueList(line,values);
    }
    @Override
    public ASTNode visitCssVariable (ProjectParser.CssVariableContext ctx){
        int line = ctx.getStart().getLine();;
        String name = ctx.getText();
        return new CSSVariable(line,name);
    }
    public ASTNode visitJinjaPrimaryExpr(ProjectParser.JinjaPrimaryExprContext ctx){
        return visit(ctx.jinjaPrimary());
    }
    private ASTNode buildBinaryExpr(ParserRuleContext ctx, String op) {
        JinjaExpression left  = (JinjaExpression) visit(ctx.getChild(0));
        JinjaExpression right = (JinjaExpression) visit(ctx.getChild(2));
        int line = ctx.getStart().getLine();

        return new JinjaBinaryExpression(line, left, right, op);
    }

    @Override
    public ASTNode visitJinjaAddExpr(ProjectParser.JinjaAddExprContext ctx){
        return buildBinaryExpr(ctx,ctx.PLUS_JINJA().getText());
    }
    @Override
    public ASTNode visitJinjaSubExpr(ProjectParser.JinjaSubExprContext ctx){
        return buildBinaryExpr(ctx, ctx.MINUS_JINJA().getText());
    }
    @Override
    public ASTNode visitJinjaMulExpr(ProjectParser.JinjaMulExprContext ctx){
        return buildBinaryExpr(ctx,ctx.MULTI_JINJA().getText());
    }
    @Override
    public ASTNode visitJinjaEqExpr(ProjectParser.JinjaEqExprContext ctx){
        return buildBinaryExpr(ctx,ctx.EQUALS_EQUALS_JINJA().getText());
    }
    @Override
    public ASTNode visitJinjaNeqExpr(ProjectParser.JinjaNeqExprContext ctx){
        return buildBinaryExpr(ctx,ctx.NOT_EQUALS_JINJA().getText());
    }
    @Override
    public ASTNode visitJinjaIdentifier(ProjectParser.JinjaIdentifierContext ctx) {
        int line = ctx.getStart().getLine();
        if (ctx.IDENTIFIER_JINJA().size() == 1) {
            String name = ctx.IDENTIFIER_JINJA(0).getText();
            if (name.equals("true")) {
                return new JinjaBooleanExpression(line, true);
            }
            if (name.equals("false")) {
                return new JinjaBooleanExpression(line, false);
            }
        }
        List<String> parts = new ArrayList<>();
        for (var id : ctx.IDENTIFIER_JINJA()) {
            parts.add(id.getText());
        }
        return new JinjaIdentifier(line, parts);
    }
    @Override
    public ASTNode visitJinjaNumber(ProjectParser.JinjaNumberContext ctx) {
        int line = ctx.getStart().getLine();
        int value = Integer.parseInt(ctx.NUMBER_JINJA().getText());
        return new JinjaNumber(line, value);
    }
    @Override
    public ASTNode visitJinjaParenExpr(ProjectParser.JinjaParenExprContext ctx) {
        int line = ctx.getStart().getLine();
        JinjaExpression inner = (JinjaExpression) visit(ctx.jinjaExpression());
        return new JinjaParenthesesExpression(line, inner);
    }
    @Override
    public ASTNode visitJinjaString(ProjectParser.JinjaStringContext ctx) {
        int line = ctx.getStart().getLine();
        String raw = ctx.STRING_JINJA().getText();
        String value = raw.substring(1, raw.length() - 1);
        return new JinjaString(line, value);
    }
    @Override
    public ASTNode visitTextNode(ProjectParser.TextNodeContext ctx) {
        int line = ctx.getStart().getLine();
        String text = ctx.HTML_TEXT().getText();
        return new TextStatement(line, text);
    }
    @Override
    public ASTNode visitVariable(ProjectParser.VariableContext ctx) {
        int line = ctx.getStart().getLine();
        JinjaExpression expr = (JinjaExpression) visit(ctx.jinjaExpression());
        return new JinjaVariableStatement(line, expr);
    }
    @Override
    public ASTNode visitJinjaForStatement(ProjectParser.JinjaForStatementContext ctx) {
        int line = ctx.getStart().getLine();

        String variable = ctx.IDENTIFIER_JINJA().getText();
        JinjaExpression iterable =
                (JinjaExpression) visit(ctx.jinjaExpression());

        List<Statement> body = new ArrayList<>();
        for (ProjectParser.NodeContext nodeCtx : ctx.node()) {
            ASTNode node = visit(nodeCtx);
            if (node instanceof Statement stmt) {
                body.add(stmt);
            }
        }

        return new JinjaForStatement(line, variable, iterable, body);
    }
    @Override
    public ASTNode visitJinjaIfStatement(ProjectParser.JinjaIfStatementContext ctx) {
        int line = ctx.getStart().getLine();

        JinjaExpression condition =
                (JinjaExpression) visit(ctx.jinjaExpression());
        List<Statement> thenBody = new ArrayList<>();
        for (ProjectParser.NodeContext nodeCtx : ctx.node()) {
            ASTNode node = visit(nodeCtx);
            if (node instanceof Statement stmt) {
                thenBody.add(stmt);
            }
        }
        List<JinjaElifStatement> elifBlocks = new ArrayList<>();
        for (ProjectParser.ElifBlockContext elifCtx : ctx.elifBlock()) {
            JinjaElifStatement elif =
                    (JinjaElifStatement) visit(elifCtx);
            elifBlocks.add(elif);
        }
        JinjaElseStatement elseBlock = null;
        if (ctx.elseBlock() != null) {
            elseBlock = (JinjaElseStatement) visit(ctx.elseBlock());
        }

        return new JinjaIfStatement(
                line,elseBlock,elifBlocks,thenBody,condition
        );
    }
    @Override
    public ASTNode visitJinjaElifBlock(ProjectParser.JinjaElifBlockContext ctx) {
        int line = ctx.getStart().getLine();

        JinjaExpression condition =
                (JinjaExpression) visit(ctx.jinjaExpression());

        List<Statement> body = new ArrayList<>();
        for (ProjectParser.NodeContext nodeCtx : ctx.node()) {
            ASTNode node = visit(nodeCtx);
            if (node instanceof Statement stmt) {
                body.add(stmt);
            }
        }

        return new JinjaElifStatement(line, condition, body);
    }
    @Override
    public ASTNode visitJinjaElseBlock(ProjectParser.JinjaElseBlockContext ctx) {
        int line = ctx.getStart().getLine();

        List<Statement> body = new ArrayList<>();
        for (ProjectParser.NodeContext nodeCtx : ctx.node()) {
            ASTNode node = visit(nodeCtx);
            if (node instanceof Statement stmt) {
                body.add(stmt);
            }
        }

        return new JinjaElseStatement(line, body);
    }

}
