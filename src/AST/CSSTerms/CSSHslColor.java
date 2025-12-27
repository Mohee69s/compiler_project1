package AST.CSSTerms;

import AST.CSSTerm;

public class CSSHslColor extends CSSTerm {
    public String value;
    protected CSSHslColor(int line, String value) {
        super(line, "CSSHslColor");
        this.value=value;
    }
    @Override
    public String toString() {
        return "hsl(" + value + ")";
    }

    public String prettyPrint(int level) {
        return indent(level) + nodeName + " (line " + line + ") hsl(" + value + ")\n";
    }
}
