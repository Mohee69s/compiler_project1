package SymbolTable.PyFlask;

public class Symbol {
    public enum SymbolType {
        VARIABLE,
        FUNCTION,
        PARAMETER,
        IMPORT,
        LOOP_VAR,
        BUILTIN
    }

    private final String name;
    private final SymbolType type;
    private final int line;
    private final String scopeName;
    private final boolean isDefined;

    public Symbol(String name, SymbolType type, int line, String scopeName) {
        this(name, type, line, scopeName, true);
    }

    public Symbol(String name, SymbolType type, int line, String scopeName, boolean isDefined) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.scopeName = scopeName;
        this.isDefined = isDefined;
    }

    public String getName() {
        return name;
    }

    public SymbolType getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public String getScopeName() {
        return scopeName;
    }

    public boolean isDefined() {
        return isDefined;
    }

    @Override
    public String toString() {
        return String.format("Symbol{name='%s', type=%s, line=%d, scope='%s'}", 
                name, type, line, scopeName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Symbol symbol = (Symbol) obj;
        return name.equals(symbol.name) && type == symbol.type;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31 + type.hashCode();
    }
}

