package AST;

public abstract class ASTNode {
    protected final int line;
    protected final String nodeName;
    protected ASTNode(int line, String nodeName){
        this.line=line;
        this.nodeName=nodeName;
    }
    public int getLine(){
        return line;
    }
    public String getNodeName(){
        return nodeName;
    }
    protected String indent(int level) {
        return "  ".repeat(level);
    }

    public String prettyPrint(int indent) {
        return null;
    }

    @Override
    public abstract String toString();
}
