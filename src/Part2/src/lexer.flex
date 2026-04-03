package Part2.lib;

import java_cup.runtime.Symbol;

%%

%class Lexer
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

"if"            { return symbol(sym.IF); }
"else"          { return symbol(sym.ELSE); }

"is-prefix-of"  { return symbol(sym.PREFIX); }
"is-suffix-of"  { return symbol(sym.SUFFIX); }

"+"             { return symbol(sym.PLUS); }
","             { return symbol(sym.COMMA); }
"("             { return symbol(sym.LPAREN); }
")"             { return symbol(sym.RPAREN); }
"{"             { return symbol(sym.LBRACE); }
"}"             { return symbol(sym.RBRACE); }

{STRING}        { return symbol(sym.STRING, yytext()); }
{IDENTIFIER}    { return symbol(sym.ID, yytext()); }

[ \t\r\n]+      { /* ignore */ }

.               { throw new Error("Unexpected char: " + yytext()); }