import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class represents the grammar of the language.
 * It holds the rules of the language.
 */
public class Grammar {

    private final int RULES_NUMBER = 31;
    private final String DELIMITOR = ";";
    private Rule[] rules = new Rule[RULES_NUMBER];
    private FortressTypeMatcher typeMatcher = new FortressTypeMatcher();

    public Grammar() {
        parseGrammar();
    }

    /*
        Read grammar file and create its rules 
    */
    private void parseGrammar() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/grammar/Fortress_grammar.txt"));
            String line = reader.readLine();
            while (line != null) {
                createRule(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        Input : String line
        The input line represents one rule, this method will
        create a rule object with its two attributs: 
        - left hand side
        - right hand side
        all elements in left or right hand side are either variables or 
        lexical units.
    */
    private void createRule(String line) {
        String elements[] = line.split(DELIMITOR, -1);
        GrammarUnit leftHandSide = new GrammarUnit(typeMatcher.getVariableType(elements[0]));
        GrammarUnit[] rightHandSide = new GrammarUnit[elements.length-1];
        Variable var;
        LexicalUnit unit;
        for (int i = 1; i < elements.length; ++i) {
            String element = elements[i];
            if (element.charAt(0) == '<' && element.charAt(element.length()-1) == '>') {
                var = typeMatcher.getVariableType(element);
                rightHandSide[i-1] = new GrammarUnit(var);
            } else {
                unit = typeMatcher.getLexicalUnitType(element);
                rightHandSide[i-1] = new GrammarUnit(unit);
            }
        }
        Rule rule = new Rule(leftHandSide, rightHandSide);
        addRule(rule);
    }

    private void addRule(Rule rule) {
        rules[rule.idx()] = rule;
    }

    public Rule getRule(int idx) {
        return rules[idx];
    }

    public Rule getRule(Variable var) {
        for (int i = 0; i < RULES_NUMBER; ++i) {
            if (rules[i].getLeftHandSide().getVariable() == var) {
                return rules[i];
            }
        }
        return null;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < RULES_NUMBER; i++) {
            s += rules[i].toString() + " ";
            s += '\n';
        }
        return s;
    }

}
