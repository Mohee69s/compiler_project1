package AST.CSSTerms;

import AST.CSSTerm;

public class CSSIdentifier extends CSSTerm {
    public String name;
    protected CSSIdentifier(int line, String Name) {
        super(line, "CSSIdentifier");
        this.name=Name;
    }
    @Override
    public String toString(){
        if (name == null) return "";
        return name;
    }

    public String prettyPrint(int level) {
        return indent(level) + nodeName + " (line " + line + ") '" + (name != null ? name : "") + "'\n";
    }
}
