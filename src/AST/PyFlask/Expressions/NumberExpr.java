package AST.PyFlask.Expressions;


import AST.PyFlask.Expression;

public class NumberExpr extends Expression {
    public String value;

    public NumberExpr(int line, String value) {
        super(line, "NumberExpr");
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String prettyPrint(int level) {
        return indent(level) + nodeName + " (line " + line + ") " + value + "\n";
    }
}
