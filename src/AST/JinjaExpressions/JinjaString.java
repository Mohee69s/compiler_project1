package AST.JinjaExpressions;

import AST.JinjaExpression;

public class JinjaString extends JinjaExpression {
    String value;
    protected JinjaString(int line, String value) {
        super(line, "JinjaString" );
        this.value=value;
    }
    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
    public String prettyPrint(int level) {
        return indent(level) + nodeName + " (line " + line + ") \"" + value + "\"\n";
    }
}
