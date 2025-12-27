package AST.CSSTerms;

import AST.CSSTerm;

public class CSSRgbaColor extends CSSTerm {
    public String value;
    protected CSSRgbaColor(int line, String value) {
        super(line, "CSSRgbaColor");
        this.value=value;
    }
    @Override
    public String toString() {
        return "rgba(" + value + ")";
    }

    public String prettyPrint(int level) {
        return indent(level) + nodeName + " (line " + line + ") rgba(" + value + ")\n";
    }
}
