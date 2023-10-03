import java.util.Map;
import java.util.HashMap;

/**
 * This class represents the action table for the parser.
 */
public class ActionTable {

    /**
     * Class to associate a LexicalUnit with a rule.
     */
    class MatchingRule {
        private LexicalUnit unit;
        private Rule rule;

        /**
         * Creates a new MatchingRule.
         * Given an index, the method will fetch the corresponding instance of the rule from the grammar.
         */ 
        public MatchingRule(LexicalUnit unit, int idx) {
            this.unit = unit;
            this.rule = grammar.getRule(idx);
        }

        public LexicalUnit getType() {
            return unit;
        }

        public Rule getRule() {
            return rule;
        }
    }

    private Map<Variable, MatchingRule[]> actionTable = new HashMap<Variable, MatchingRule[]>();
    private Grammar grammar = null;

    /**
     * Creates a new ActionTable for the Fortress language.
     */
    public ActionTable(Grammar grammar) {
        this.grammar = grammar;
        actionTable.put(Variable.PROGRAM, new MatchingRule[] {
                new MatchingRule(LexicalUnit.PROGNAME, 0)
        });

        actionTable.put(Variable.CODE, new MatchingRule[] {
                new MatchingRule(LexicalUnit.END, 2),
                new MatchingRule(LexicalUnit.VARNAME, 1),
                new MatchingRule(LexicalUnit.IF, 1),
                new MatchingRule(LexicalUnit.ELSE, 2),
                new MatchingRule(LexicalUnit.WHILE, 1),
                new MatchingRule(LexicalUnit.PRINT, 1),
                new MatchingRule(LexicalUnit.READ, 1),
        });

        actionTable.put(Variable.INSTRUCTION, new MatchingRule[] {
                new MatchingRule(LexicalUnit.VARNAME, 3),
                new MatchingRule(LexicalUnit.IF, 4),
                new MatchingRule(LexicalUnit.WHILE, 5),
                new MatchingRule(LexicalUnit.PRINT, 6),
                new MatchingRule(LexicalUnit.READ, 7),
        });

        actionTable.put(Variable.ASSIGN, new MatchingRule[] {
                new MatchingRule(LexicalUnit.VARNAME, 8),
        });

        actionTable.put(Variable.EXPRARITH, new MatchingRule[] {
                new MatchingRule(LexicalUnit.VARNAME, 9),
                new MatchingRule(LexicalUnit.MINUS, 9),
                new MatchingRule(LexicalUnit.NUMBER, 9),
                new MatchingRule(LexicalUnit.LPAREN, 9),
        });

        actionTable.put(Variable.EXPRARITHPRIME, new MatchingRule[] {
                new MatchingRule(LexicalUnit.COMMA, 12),
                new MatchingRule(LexicalUnit.PLUS, 10),
                new MatchingRule(LexicalUnit.MINUS, 11),
                new MatchingRule(LexicalUnit.RPAREN, 12),
                new MatchingRule(LexicalUnit.EQUAL, 12),
                new MatchingRule(LexicalUnit.SMALLER, 12),
                new MatchingRule(LexicalUnit.GREATER, 12),
        });

        actionTable.put(Variable.PROD, new MatchingRule[] {
                new MatchingRule(LexicalUnit.VARNAME, 13),
                new MatchingRule(LexicalUnit.MINUS, 13),
                new MatchingRule(LexicalUnit.NUMBER, 13),
                new MatchingRule(LexicalUnit.LPAREN, 13),
        });

        actionTable.put(Variable.PRODPRIME, new MatchingRule[] {
                new MatchingRule(LexicalUnit.PLUS, 16),
                new MatchingRule(LexicalUnit.MINUS, 16),
                new MatchingRule(LexicalUnit.TIMES, 14),
                new MatchingRule(LexicalUnit.DIVIDE, 15),
                // add
                new MatchingRule(LexicalUnit.COMMA, 16),
                new MatchingRule(LexicalUnit.GREATER, 16),
                new MatchingRule(LexicalUnit.SMALLER, 16),
                new MatchingRule(LexicalUnit.RPAREN, 16),
                new MatchingRule(LexicalUnit.EQUAL, 16),
        });

        actionTable.put(Variable.ATOM, new MatchingRule[] {
                new MatchingRule(LexicalUnit.VARNAME, 17),
                new MatchingRule(LexicalUnit.MINUS, 20),
                new MatchingRule(LexicalUnit.NUMBER, 18),
                new MatchingRule(LexicalUnit.LPAREN, 19),
        });

        actionTable.put(Variable.IF, new MatchingRule[] {
                new MatchingRule(LexicalUnit.IF, 21),
        });

        actionTable.put(Variable.IFSEQ, new MatchingRule[] {
                new MatchingRule(LexicalUnit.END, 22),
                new MatchingRule(LexicalUnit.ELSE, 23),
        });

        actionTable.put(Variable.COND, new MatchingRule[] {
                new MatchingRule(LexicalUnit.VARNAME, 24),
                new MatchingRule(LexicalUnit.MINUS, 24),
                new MatchingRule(LexicalUnit.NUMBER, 24),
                new MatchingRule(LexicalUnit.LPAREN, 24),
        });

        actionTable.put(Variable.COMP, new MatchingRule[] {
                new MatchingRule(LexicalUnit.EQUAL, 25),
                new MatchingRule(LexicalUnit.SMALLER, 27),
                new MatchingRule(LexicalUnit.GREATER, 26),
        });

        actionTable.put(Variable.WHILE, new MatchingRule[] {
                new MatchingRule(LexicalUnit.WHILE, 28),
        });

        actionTable.put(Variable.PRINT, new MatchingRule[] {
                new MatchingRule(LexicalUnit.PRINT, 29),
        });

        actionTable.put(Variable.READ, new MatchingRule[] {
                new MatchingRule(LexicalUnit.READ, 30),
        });

    }

    private MatchingRule[] getRules(GrammarUnit unit) {
        return actionTable.get(unit.getVariable());
    }

    /**
     * Given the current symbol and the unit from a rule, returns the rule to apply.
     */
    Rule getCorrespondingRule(Symbol s, GrammarUnit unit) {
        MatchingRule[] matchingRules = getRules(unit);
        for (int i = 0; i < matchingRules.length; ++i) {
            if (matchingRules[i].getType() == s.getType()) {
                return matchingRules[i].getRule();
            }
        }
        return null;
    }
}
