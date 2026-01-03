package SymbolTable.PyFlask;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SymbolTable {
    private Scope globalScope;
    private Stack<Scope> scopeStack;
    private List<Scope> allScopes;
    private List<String> errors;

    public SymbolTable() {
        this.globalScope = new Scope("global", Scope.ScopeType.GLOBAL, null, 1);
        this.scopeStack = new Stack<>();
        this.scopeStack.push(globalScope);
        this.allScopes = new ArrayList<>();
        this.allScopes.add(globalScope);
        this.errors = new ArrayList<>();
    }

    public void enterScope(String name, Scope.ScopeType type, int startLine) {
        Scope currentScope = scopeStack.peek();
        Scope newScope = new Scope(name, type, currentScope, startLine);
        scopeStack.push(newScope);
        allScopes.add(newScope);
    }
    public void exitScope() {
        if (scopeStack.size() > 1) {
            scopeStack.pop();
        }
    }

    public Scope getCurrentScope() {
        return scopeStack.peek();
    }
    public Scope getGlobalScope() {
        return globalScope;
    }
    public boolean addSymbol(String name, Symbol.SymbolType type, int line) {
        Scope currentScope = getCurrentScope();
        String scopeName = currentScope.getName();
        Symbol symbol = new Symbol(name, type, line, scopeName);
        
        if (!currentScope.addSymbol(symbol)) {

            errors.add(String.format("Error at line %d: Symbol '%s' already defined in scope '%s'", 
                    line, name, scopeName));
            return false;
        }
        return true;
    }

    public boolean addSymbolToGlobal(String name, Symbol.SymbolType type, int line) {
        String scopeName = globalScope.getName();
        Symbol symbol = new Symbol(name, type, line, scopeName);
        
        if (!globalScope.addSymbol(symbol)) {
            return false;
        }
        return true;
    }

    public boolean addSymbolToParent(String name, Symbol.SymbolType type, int line) {
        Scope currentScope = getCurrentScope();
        Scope parentScope = currentScope.getParent();
        if (parentScope == null) {
            return false;
        }
        String scopeName = parentScope.getName();
        Symbol symbol = new Symbol(name, type, line, scopeName);
        
        if (!parentScope.addSymbol(symbol)) {

            return false;
        }
        return true;
    }

    public Symbol lookup(String name) {
        return getCurrentScope().lookup(name);
    }

    public Symbol lookupLocal(String name) {
        return getCurrentScope().lookupLocal(name);
    }

    public boolean contains(String name) {
        return getCurrentScope().contains(name);
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public void addError(String error) {
        errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void print() {
        System.out.println("=== Symbol Table ===");
        printScope(globalScope, 0);
        if (hasErrors()) {
            System.out.println("\n=== Errors ===");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }

    private void printScope(Scope scope, int indent) {
        String indentStr = "  ".repeat(indent);
        System.out.println(indentStr + scope);
        
        for (Symbol symbol : scope.getSymbols().values()) {
            System.out.println(indentStr + "  " + symbol);
        }
        
        for (Scope childScope : allScopes) {
            if (childScope.getParent() == scope) {
                printScope(childScope, indent + 1);
            }
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Symbol Table ===\n");
        sb.append(toStringScope(globalScope, 0));
        if (hasErrors()) {
            sb.append("\n=== Errors ===\n");
            for (String error : errors) {
                sb.append(error).append("\n");
            }
        }
        return sb.toString();
    }

    private String toStringScope(Scope scope, int indent) {
        StringBuilder sb = new StringBuilder();
        String indentStr = "  ".repeat(indent);
        sb.append(indentStr).append(scope).append("\n");
        
        for (Symbol symbol : scope.getSymbols().values()) {
            sb.append(indentStr).append("  ").append(symbol).append("\n");
        }
        for (Scope childScope : allScopes) {
            if (childScope.getParent() == scope) {

                sb.append(toStringScope(childScope, indent + 1));
            }
        }
        
        return sb.toString();
    }
}

