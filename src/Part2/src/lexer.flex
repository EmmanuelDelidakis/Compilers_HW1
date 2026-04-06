package Part2.src;

import java_cup.runtime.Symbol;

%%

%class Lexer
%public
%unicode
%cup
%line
%column

%{

private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
}

private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
}

%}

IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*
STRING = \"([^\"\\]|\\.)*\"

%%

"if"            { return symbol(Part2.src.sym.IF); }
"else"          { return symbol(Part2.src.sym.ELSE); }
"def"           { return symbol(Part2.src.sym.DEF); }

"is-prefix-of"  { return symbol(Part2.src.sym.PREFIX); }
"is-suffix-of"  { return symbol(Part2.src.sym.SUFFIX); }

";"             { return symbol(Part2.src.sym.SEMICOLON); }
"+"             { return symbol(Part2.src.sym.PLUS); }
","             { return symbol(Part2.src.sym.COMMA); }
"("             { return symbol(Part2.src.sym.LPAREN); }
")"             { return symbol(Part2.src.sym.RPAREN); }
"{"             { return symbol(Part2.src.sym.LBRACE); }
"}"             { return symbol(Part2.src.sym.RBRACE); }

{STRING}        { return symbol(Part2.src.sym.STRING, yytext()); }
{IDENTIFIER}    { return symbol(Part2.src.sym.ID, yytext()); }

[ \t\r\n]+      { /* ignore whitespace */ }

.               { throw new Error("Unexpected char: " + yytext()); }
