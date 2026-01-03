lexer grammar PythonLexer;

// lexer
BOOL: 'true'|'false';
PRINT: 'print';
DEF: 'def';
RETURN: 'return';
IMPORT: 'import';
FROM: 'from';
AT: '@';
ASSIGN: '=';
COMMA:',';
DOT: '.';
COLON: ':';
LPARENS: '(';
RPARENS: ')';
LSB: '[';
RSB: ']';
LBK: '{';
RBK: '}';
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
LT: '<';
GT: '>';
LE: '<=';
GE: '>=';
EQ: '==';
NE: '!=';
IF: 'if';
ELSE: 'else';
FOR: 'for';
IN: 'in';
WHILE: 'while';
VAR: 'var';


ID: [a-zA-Z_][a-zA-Z_0-9]*;
NUMBER: '0'|[1-9][0-9]*;
STRING: '"' (~["\\] | '\\' .)* '"';
NEWLINE: ('\r'? '\n')+ ;
INDENT: '<INDENT>';
DEDENT: '<DEDENT>';
//NEWLINE: ('\r'? '\n' (' ' | '\t')*);
WS: [ \t]+ -> skip;
COMMENT: '#' ~[\r\n]* -> skip;
