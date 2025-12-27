package AST.CSSTerms;

import AST.CSSTerm;

public class CSSColor extends CSSTerm {
    public final String value;
    protected CSSColor(int line, String value) {
        super(line, "CSSColor");
        this.value=value;
    }
    @Override
    public String toString() {
        if (value == null) return "";
        return value; // Named colors like "red", "blue" shouldn't be quoted
    }

    public String prettyPrint(int level) {
        return indent(level) + nodeName + " (line " + line + ") \"" + value + "\"\n";
    }
}
