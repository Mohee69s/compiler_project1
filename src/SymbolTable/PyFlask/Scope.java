package SymbolTable.PyFlask;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final String name;
    private final ScopeType type;
    private final Scope parent;
    private final Map<String, Symbol> symbols;
    private final int startLine;

    public enum ScopeType {
        GLOBAL,
        FUNCTION,
        BLOCK
    }

    public Scope(String name, ScopeType type, Scope parent, int startLine) {
        this.name = name;
        this.type = type;
        this.parent = parent;
        this.symbols = new HashMap<>();
        this.startLine = startLine;
    }

    public boolean addSymbol(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) {
            return false;
        }
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    public Symbol lookupLocal(String name) {
        return symbols.get(name);
    }
    public Symbol lookup(String name) {
        Symbol symbol = symbols.get(name);
        if (symbol != null) {
            return symbol;
        }
        if (parent != null) {
            return parent.lookup(name);
        }
        return null;
    }

    public boolean containsLocal(String name) {
        return symbols.containsKey(name);
    }

    public boolean contains(String name) {
        return lookup(name) != null;
    }

    public String getName() {
        return name;
    }

    public ScopeType getType() {
        return type;
    }

    public Scope getParent() {
        return parent;
    }

    public Map<String, Symbol> getSymbols() {
        return new HashMap<>(symbols);
    }

    public int getStartLine() {
        return startLine;
    }

    @Override
    public String toString() {
        return String.format("Scope{name='%s', type=%s, symbols=%d, startLine=%d}", 
                name, type, symbols.size(), startLine);
    }
}

