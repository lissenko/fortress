/**
 * This class represent a grammar unit.
 * A grammar Unit can be either a variable or a lexical unit.
 * It can also hold a value.
 */
public class GrammarUnit {
    private LexicalUnit lexicalUnit = null;
    private Variable variable = null;
    private Symbol symbol = null;

    private GrammarUnit(LexicalUnit lexicalUnit, Variable variable, Symbol symbol) {
        this.lexicalUnit = lexicalUnit; // duplicate information
        this.variable = variable;
        this.symbol = symbol;
    }

    public GrammarUnit(Variable variable) {
        this(null, variable, null);
    }

    public GrammarUnit(LexicalUnit lexicalUnit) {
        this(lexicalUnit, null, null);
    }

    public GrammarUnit(Symbol symbol) {
        this(symbol.getType(), null, symbol);
    }

    public boolean isVariable() {
        return this.lexicalUnit == null;
    }

    public boolean isSymbole() {
        return this.symbol == null;
    }

    public boolean hasValue() {
        if(this.symbol != null) {
            return this.symbol.hasValue();
        }
        return false;
    }

    public void modifyValue(String value) {
        this.symbol.modifyValue(value);
    }

    public Variable getVariable() {
        return this.variable;
    }

    public LexicalUnit getType() {
        return this.lexicalUnit;
    }

    public Symbol getSymbol() {
        return this.symbol;
    }

    public String getString() {
        return this.symbol.getValue().toString();
    }

    public boolean isArith() {
        if (this.symbol != null) {
            return this.symbol.isArith();
        }
        return false;
    }

    public void addMinus() {
        if (this.symbol != null) {
            this.symbol.addMinus();
        }
    }

    public String toString() {
        if (symbol != null) {
            return "'" + symbol.getValue().toString()+ "'";
        } else if (lexicalUnit != null) {
            return lexicalUnit.toString();
        } else {
            return "$<$" + variable.toString() + "$>$";
        }
    }

    public String toTexString() {
        if (symbol != null) {
            if (symbol.getType() == LexicalUnit.GREATER) {
                return "$>$";
            } else if (symbol.getType() == LexicalUnit.SMALLER) {
                return "$<$";
            }
        }
        return this.toString();
    }

}
