import java_cup.runtime.*;

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

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Identifier     = [a-zA-Z_][a-zA-Z0-9_]*
StringLiteral  = \"([^\\\"]|\\.)*\"
Comment        = "//"[^\r\n]*

%%

<YYINITIAL> {
    /* Keywords */
    "def"       { return symbol(sym.DEF); }
    "if"        { return symbol(sym.IF); }
    "else"      { return symbol(sym.ELSE); }
    "prefix"    { return symbol(sym.PREFIX); }
    "suffix"    { return symbol(sym.SUFFIX); }

    /* Operators and punctuation */
    "+"         { return symbol(sym.PLUS); }
    "("         { return symbol(sym.LPAREN); }
    ")"         { return symbol(sym.RPAREN); }
    ","         { return symbol(sym.COMMA); }
    ":"         { return symbol(sym.COLON); }
    ";"         { return symbol(sym.SEMICOLON); }

    /* Literals */
    {StringLiteral} {
        String s = yytext();
        // strip surrounding quotes
        s = s.substring(1, s.length() - 1);
        return symbol(sym.STRING_LIT, s);
    }

    /* Identifiers */
    {Identifier} { return symbol(sym.ID, yytext()); }

    /* Whitespace / comments */
    {WhiteSpace} { /* skip */ }
    {Comment}    { /* skip */ }
}

/* Error fallback */
[^] { throw new Error("Illegal character: <" + yytext() + "> at line " + yyline + ", col " + yycolumn); }
