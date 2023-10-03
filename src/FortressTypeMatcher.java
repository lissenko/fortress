/**
 * Match every keyword in the file representing the Fortress Grammar.
 */
public class FortressTypeMatcher {

    /*
     * Match the lexical units
     */
    public LexicalUnit getLexicalUnitType(String unit) {
        switch (unit) {
            case "BEGIN":
                return LexicalUnit.BEGIN;
            case "ProgName":
                return LexicalUnit.PROGNAME;
            case "END":
                return LexicalUnit.END;
            case ",":
                return LexicalUnit.COMMA;
            case "E":
                return LexicalUnit.EPSILON;
            case "[VarName]":
                return LexicalUnit.VARNAME;
            case "+":
                return LexicalUnit.PLUS;
            case ":=":
                return LexicalUnit.ASSIGN;
            case "=":
                return LexicalUnit.EQUAL;
            case "-":
                return LexicalUnit.MINUS;
            case "*":
                return LexicalUnit.TIMES;
            case "/":
                return LexicalUnit.DIVIDE;
            case "[number]":
                return LexicalUnit.NUMBER;
            case "(":
                return LexicalUnit.LPAREN;
            case ")":
                return LexicalUnit.RPAREN;
            case "IF":
                return LexicalUnit.IF;
            case "THEN":
                return LexicalUnit.THEN;
            case "ELSE":
                return LexicalUnit.ELSE;
            case "<":
                return LexicalUnit.SMALLER;
            case ">":
                return LexicalUnit.GREATER;
            case "DO":
                return LexicalUnit.DO;
            case "WHILE":
                return LexicalUnit.WHILE;
            case "PRINT":
                return LexicalUnit.PRINT;
            case "READ":
                return LexicalUnit.READ;
        }
        return null;
    }

    /*
     * Match the variables.
     */
    public Variable getVariableType(String variable) {
        switch (variable) {
            case "<Program>":
                return Variable.PROGRAM;
            case "<Code>":
                return Variable.CODE;
            case "<Instruction>":
                return Variable.INSTRUCTION;
            case "<Assign>":
                return Variable.ASSIGN;
            case "<ExprArith>":
                return Variable.EXPRARITH;
            case "<ExprArith'>":
                return Variable.EXPRARITHPRIME;
            case "<Prod>":
                return Variable.PROD;
            case "<Prod'>":
                return Variable.PRODPRIME;
            case "<Atom>":
                return Variable.ATOM;
            case "<IF>":
                return Variable.IF;
            case "<IfSeq>":
                return Variable.IFSEQ;
            case "<Cond>":
                return Variable.COND;
            case "<Comp>":
                return Variable.COMP;
            case "<While>":
                return Variable.WHILE;
            case "<Print>":
                return Variable.PRINT;
            case "<Read>":
                return Variable.READ;
        }
        return null;
    }
}
