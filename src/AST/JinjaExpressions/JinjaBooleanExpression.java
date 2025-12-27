package AST.JinjaExpressions;

import AST.JinjaExpression;

public class JinjaBooleanExpression extends JinjaExpression {
    private Boolean value;
    protected JinjaBooleanExpression(int line, Boolean value) {
        super(line, "JinjaBooleanExpression");
        this.value=value;
    }
    @Override
    public String toString(){
        if (value == null) return "null";
        return value.toString();
    }

    public String prettyPrint(int level) {
        String val = (value == null) ? "null" : value.toString();
        return indent(level) + nodeName + " (line " + line + ") " + val + "\n";
    }
}
