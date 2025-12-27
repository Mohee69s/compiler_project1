package AST.CSSTerms;

import AST.CSSTerm;

public class CSSRgbColor extends CSSTerm {
    public String value;
    protected CSSRgbColor(int line, String value) {
        super(line, "CSSRgbColor");
        this.value=value;
    }
    @Override
    public String toString() {
        return "rgb(" + value + ")";
    }

    public String prettyPrint(int level) {
        return indent(level) + nodeName + " (line " + line + ") rgb(" + value + ")\n";
    }
}
