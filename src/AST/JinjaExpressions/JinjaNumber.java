package AST.JinjaExpressions;

import AST.JinjaExpression;

public class JinjaNumber extends JinjaExpression {
    protected int value;
    protected JinjaNumber(int line, int value) {
        super(line, "JinjaNumber");
        this.value=value;
    }

    @Override
    public String toString(){
        return Integer.toString(value);
    }

    public String prettyPrint(int level){
        StringBuilder sb = new StringBuilder();
        sb.append(indent(level)).append(nodeName).append(" '").append(value).append("' (line ").append(line).append(")\n");
        return sb.toString();
    }
}
