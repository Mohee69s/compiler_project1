package AST.CSSTerms;

import AST.CSSTerm;

public class CSSCalc extends CSSTerm {
    public String expression;
    protected CSSCalc(int line, String expression) {
        super(line, "CSSCalc");
        this.expression = expression;
    }
    @Override
    public String toString() {
        return "calc(" + expression + ")";
    }

    public String prettyPrint(int level) {
        return indent(level) + nodeName + " (line " + line + ") calc(" + expression + ")\n";
    }
}
