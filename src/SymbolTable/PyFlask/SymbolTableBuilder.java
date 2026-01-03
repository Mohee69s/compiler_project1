package SymbolTable.PyFlask;

import AST.ASTNode;
import AST.PyFlask.Block;
import AST.PyFlask.Expression;
import AST.PyFlask.Expressions.*;
import AST.Program;
import AST.Statement;
import AST.PyFlask.Statements.*;
public class SymbolTableBuilder {
  private SymbolTable symbolTable;
  private boolean inIfBlock = false;

  public SymbolTableBuilder() {
    this.symbolTable = new SymbolTable();
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public void visit(ASTNode node) {
    if (node == null)
      return;

    if (node instanceof Program) {
      visitProgram((Program) node);
    } else if (node instanceof Block) {
      visitBlock((Block) node);
    } else if (node instanceof FunctionDef) {
      visitFunctionDef((FunctionDef) node);
    } else if (node instanceof AssignStmt) {
      visitAssignStmt((AssignStmt) node);
    } else if (node instanceof IfStmt) {
      visitIfStmt((IfStmt) node);
    } else if (node instanceof WhileStmt) {
      visitWhileStmt((WhileStmt) node);
    } else if (node instanceof ForStmt) {
      visitForStmt((ForStmt) node);
    } else if (node instanceof ImportStmt) {
      visitImportStmt((ImportStmt) node);
    } else if (node instanceof PrintStmt) {
      visitPrintStmt((PrintStmt) node);
    } else if (node instanceof ReturnStmt) {
      visitReturnStmt((ReturnStmt) node);
    } else if (node instanceof ExprStmt) {
      visitExprStmt((ExprStmt) node);
    } else if (node instanceof IdentifierExpr) {
      visitIdentifierExpr((IdentifierExpr) node);
    } else if (node instanceof FunctionCallExpr) {
      visitFunctionCallExpr((FunctionCallExpr) node);
    } else if (node instanceof BinaryExpr) {
      visitBinaryExpr((BinaryExpr) node);
    } else if (node instanceof ArrayLiteral) {
      visitArrayLiteral((ArrayLiteral) node);
    } else if (node instanceof DictLiteral) {
      visitDictLiteral((DictLiteral) node);
    } else if (node instanceof IndexExpr) {
      visitIndexExpr((IndexExpr) node);
    } else if (node instanceof AttributeExpr) {
      visitAttributeExpr((AttributeExpr) node);
    } else if (node instanceof KeyValue) {
      visitKeyValue((KeyValue) node);
    } else if (node instanceof NumberExpr) {
      visitNumberExpr((NumberExpr) node);
    } else if (node instanceof StringExpr) {
      visitStringExpr((StringExpr) node);
    } else if (node instanceof BooleanExpr) {
      visitBooleanExpr((BooleanExpr) node);
    }
  }

  private void visitProgram(Program node) {
    for (Statement stmt : node.getStatements()) {
      visit(stmt);
    }
  }

  private void visitBlock(Block node) {
    String scopeName = "block_" + node.getLine();
    symbolTable.enterScope(scopeName, Scope.ScopeType.BLOCK, node.getLine());

    for (Statement stmt : node.getStatements()) {
      visit(stmt);
    }

    symbolTable.exitScope();
  }

  private void visitFunctionDef(FunctionDef node) {
    symbolTable.addSymbol(node.name, Symbol.SymbolType.FUNCTION, node.getLine());

    String scopeName = "func_" + node.name;
    symbolTable.enterScope(scopeName, Scope.ScopeType.FUNCTION, node.getLine());

    if (node.parameters != null) {
      for (String param : node.parameters) {
        String[] params = param.split(",");
        for (String p : params) {
          String trimmed = p.trim();
          if (!trimmed.isEmpty()) {
            symbolTable.addSymbol(trimmed, Symbol.SymbolType.PARAMETER, node.getLine());
          }
        }
      }
    }

    if (node.body != null) {
      for (Statement stmt : node.body.getStatements()) {
        visit(stmt);
      }
    }

    symbolTable.exitScope();
  }

  private void visitAssignStmt(AssignStmt node) {
    if (node.name instanceof IdentifierExpr) {
      IdentifierExpr id = (IdentifierExpr) node.name;

      if (inIfBlock) {
        Scope parentScope = symbolTable.getCurrentScope().getParent();
        Symbol existing = parentScope != null ? parentScope.lookupLocal(id.name) : null;

        if (existing == null) {
          symbolTable.addSymbolToParent(id.name, Symbol.SymbolType.VARIABLE, node.getLine());
        }
      } else {
        Symbol existing = symbolTable.lookupLocal(id.name);

        if (existing == null) {
          symbolTable.addSymbol(id.name, Symbol.SymbolType.VARIABLE, node.getLine());
        }
      }
    }
    visit(node.value);
  }

  private void visitIfStmt(IfStmt node) {
    visit(node.condition);

    if (node.thenBlock != null) {
      String scopeName = "block_" + node.thenBlock.getLine();
      symbolTable.enterScope(scopeName, Scope.ScopeType.BLOCK, node.thenBlock.getLine());

      boolean oldInIfBlock = inIfBlock;
      inIfBlock = true;

      for (Statement stmt : node.thenBlock.getStatements()) {
        visit(stmt);
      }

      inIfBlock = oldInIfBlock;

      symbolTable.exitScope();
    }

    if (node.elseBlock != null) {
      String scopeName = "block_" + node.elseBlock.getLine();
      symbolTable.enterScope(scopeName, Scope.ScopeType.BLOCK, node.elseBlock.getLine());

      boolean oldInIfBlock = inIfBlock;
      inIfBlock = true;

      for (Statement stmt : node.elseBlock.getStatements()) {
        visit(stmt);
      }

      inIfBlock = oldInIfBlock;
      symbolTable.exitScope();
    }
  }

  private void visitWhileStmt(WhileStmt node) {
    visit(node.condition);
    if (node.body != null) {
      for (Statement stmt : node.body.getStatements()) {
        visit(stmt);
      }
    }
  }

  private void visitForStmt(ForStmt node) {
    if (node.loopVariable instanceof IdentifierExpr) {
      IdentifierExpr loopVar = (IdentifierExpr) node.loopVariable;
      Symbol existing = symbolTable.lookupLocal(loopVar.name);
      if (existing == null) {
        symbolTable.addSymbol(loopVar.name, Symbol.SymbolType.LOOP_VAR, node.getLine());
      }
    }

    visit(node.iterable);

    if (node.body != null) {
      for (Statement stmt : node.body.getStatements()) {
        visit(stmt);
      }
    }
  }

  private void visitImportStmt(ImportStmt node) {

    if (node.names != null) {
      for (String name : node.names) {
        symbolTable.addSymbol(name, Symbol.SymbolType.IMPORT, node.getLine());
      }
    }
  }

  private void visitIdentifierExpr(IdentifierExpr node) {
    if ("__name__".equals(node.name)) {
      Symbol existing = symbolTable.getGlobalScope().lookupLocal("__name__");
      if (existing == null) {
        symbolTable.addSymbolToGlobal("__name__", Symbol.SymbolType.BUILTIN, node.getLine());
      }
      return;
    }

    Symbol symbol = symbolTable.lookup(node.name);
    if (symbol == null) {

      symbolTable.addError(String.format("Warning at line %d: Undefined identifier '%s'",
          node.getLine(), node.name));
    }
  }

  private void visitFunctionCallExpr(FunctionCallExpr node) {

    if (node.callee instanceof IdentifierExpr) {
      IdentifierExpr id = (IdentifierExpr) node.callee;
      Symbol symbol = symbolTable.lookup(id.name);
      if (symbol == null || symbol.getType() != Symbol.SymbolType.FUNCTION) {
        symbolTable.addError(String.format("Warning at line %d: Function '%s' may not be defined",
            node.getLine(), id.name));
      }
    }

    if (node.args != null) {
      for (Expression arg : node.args) {
        visit(arg);
      }
    }
  }

  private void visitBinaryExpr(BinaryExpr node) {
    visit(node.left);
    visit(node.right);
  }

  private void visitArrayLiteral(ArrayLiteral node) {
    if (node.elements != null) {
      for (Expression item : node.elements) {
        visit(item);
      }
    }
  }

  private void visitDictLiteral(DictLiteral node) {
    if (node.entries != null) {
      for (KeyValue pair : node.entries) {
        visit(pair.key);
        visit(pair.value);
      }
    }
  }

  private void visitIndexExpr(IndexExpr node) {
    visit(node.array);
    visit(node.index);
  }

  private void visitAttributeExpr(AttributeExpr node) {
    visit(node.target);
  }

  private void visitKeyValue(KeyValue node) {
    visit(node.key);
    visit(node.value);
  }

  private void visitNumberExpr(NumberExpr node) {
  }

  private void visitStringExpr(StringExpr node) {
  }

  private void visitBooleanExpr(BooleanExpr node) {

  }

  private void visitPrintStmt(PrintStmt node) {
    visit(node.expr);
  }

  private void visitReturnStmt(ReturnStmt node) {
    if (node.expr != null) {
      visit(node.expr);
    }
  }

  private void visitExprStmt(ExprStmt node) {
    visit(node.expr);
  }
}
