parser grammar PythonParser;

options { tokenVocab = PythonLexer; }

// -------------------------------------------------
// PROGRAM
// -------------------------------------------------
prog
    : stat* EOF
    ;

// -------------------------------------------------
// STATEMENTS
// -------------------------------------------------
stat
    : assignStat
    | printStat
    | ifStat
    | forStat
    | whileStat
    | funcDef
    | returnStat
    | importStat
    | exprStat
    | arrayAssignStat
    | NEWLINE
    ;

// print
printStat
    : PRINT LPARENS expr RPARENS NEWLINE?              # PrintStatement
    ;

// assignment
assignStat
    : VAR? ID ASSIGN expr NEWLINE?                     # Assignment
    ;

// standalone expression
exprStat
    : expr NEWLINE?
    ;

// return
returnStat
    : RETURN expr NEWLINE?                             # ReturnStatement
    ;

arrayAssignStat
    : expr LSB expr RSB ASSIGN expr NEWLINE?    # ArrayAssignment
    ;
// -------------------------------------------------
// IMPORTS
// -------------------------------------------------
importStat
    : IMPORT dottedName (COMMA dottedName)* NEWLINE #ImportModule
    | FROM dottedName IMPORT ID (COMMA ID)* NEWLINE #ImportFrom
    ;

dottedName
    : ID (DOT ID)*
    ;

// -------------------------------------------------
// FUNCTIONS
// -------------------------------------------------
funcDef
    : decorator* DEF ID LPARENS paramList? RPARENS COLON? NEWLINE block # FunctionDefinition
    ;

paramList
    : ID (COMMA ID)*               # ParameterList
    ;

decorator
    : AT dottedName LPARENS argList? RPARENS NEWLINE       # DecoratorWithArgs
    | AT dottedName NEWLINE                                # DecoratorNoArgs
    ;

// -------------------------------------------------
// CONTROL FLOW
// -------------------------------------------------
ifStat
    : IF expr COLON NEWLINE block (ELSE NEWLINE block)?
    ;

forStat
    : FOR expr IN expr COLON NEWLINE block                # ForStatement
    ;

whileStat
    : WHILE expr NEWLINE block                             # WhileStatement
    ;

block
    : INDENT stat+ DEDENT
    ;

// -------------------------------------------------
// EXPRESSIONS
// -------------------------------------------------
expr
    : expr op=(MUL|DIV) expr                     # MultDivExpr
    | expr op=(ADD|SUB) expr                     # AddSubExpr
    | expr op=(LT|GT|GE|LE|EQ|NE) expr           # ComparisonExpr
    | expr ASSIGN expr                           # KeyValue
    | expr LSB expr RSB                          # IndexExprz
    | ID LPARENS argList? RPARENS                # FunctionCallExpr
    | expr DOT ID                                # AttributeExpr
    | LSB argList? RSB                           # ArrayLiteral
    | LPARENS expr RPARENS                       # ParenExpr
    | LBK dictBody? RBK                          # DictLiteral
    | NUMBER                                     # NumberLiteral
    | STRING                                     # StringLiteral
    | BOOL                                       # BooleanLiteral
    | ID                                         # IdentifierExpr
    ;

dictBody: NEWLINE* pair (NEWLINE* ',' NEWLINE* pair)* NEWLINE*;

pair: STRING COLON expr;


argList : (arg (COMMA arg)*)?;
arg : expr | ID ASSIGN expr;
