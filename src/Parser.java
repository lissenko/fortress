import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * This class is used to parse the input file using the grammar and the action table.
 * It uses a top-down recursive parsing algorithm.
 */
public class Parser {

    private Grammar grammar = new Grammar();
    private Lexer lexer = null;
    private Symbol symbolPending = null;
    private ParseTree parseTree = null;
    private ActionTable actionTable = new ActionTable(grammar);
    private ArrayList<Rule> rules = new ArrayList<Rule>();

    private Ast ast = null;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Symbol getNextChar() throws IOException {
        Symbol s = null;
        if (symbolPending == null) {
            s = lexer.nextToken();
        } else {
            s = symbolPending;
            symbolPending = null;
        }
        return s;
    }

    public Symbol getNextCharDontConsume() throws IOException {
        if (symbolPending == null) {
            symbolPending = lexer.nextToken();
        }
        return symbolPending;
    }

    /*
        Input: rule, parent
        Each symbol at the right hand side of the rule will be treated
        if it's a variable, there will be a recursive call with a new rule getted
        from the action table.
        when the symbole is a lexical unit, it's simpley added to the tree (if there is a match)
    */
    private void recurse(Rule rule, ParseTree parent) throws IOException {
        GrammarUnit[] right = rule.getRight();
        parent.setChildren(right);
        for (int i = 0; i < right.length; i++) {
            GrammarUnit unit = right[i];
            Symbol nextSymbol = null;
            if (unit.isVariable()) {
                nextSymbol = getNextCharDontConsume();
                Rule nextRule = actionTable.getCorrespondingRule(nextSymbol, unit);
                if (nextRule == null) {
                    throw new Error("No rule in action table");
                }
                rules.add(nextRule);
                if (!nextRule.isEpsilon()) {
                    recurse(nextRule, parent.get(i));
                }
            } else {
                nextSymbol = getNextChar();
                if (unit.getType() != nextSymbol.getType()) {
                    throw new Error("Inputs are not matching !");
                } else {
                    GrammarUnit[] units = {new GrammarUnit(nextSymbol)};
                    parent.get(i).setChildren(units);
                }
            }
        }
    }

    /*
        Prints left derivation rules
    */
    public void left_derivation() {
        for (Rule rule: rules) {
            System.out.print(rule);
        }
        System.out.print("\n");
    }

    public void parse() throws IOException {
        Rule start = grammar.getRule(0);
        rules.add(start);
        parseTree = new ParseTree(start.getLeftHandSide());
        recurse(start, parseTree);
        if (getNextChar().getType() == LexicalUnit.EOS) {
            // Warning: uncommenting this line will print the left derivation
            // left_derivation();
            this.ast = new Ast(parseTree);
            this.ast.create();
            LlvmGenerator llvmGenerator = new LlvmGenerator(this.ast.getRoot());
            llvmGenerator.generate();
        } else {
            throw new Error("Parsing unsuccessfull");

        }
    }

    /**
     * Create a tex file with the parse tree.
     * Output are located in the tex/ folder.
     */
    public void createTreeFile(String texFile) throws IOException {
        Instruction instruction = this.ast.getRoot().getThen(0);
        ParseTree tree = instruction.getTree();
        String str = tree.toLaTeX();
        File dir = new File("tex/");
        File file = new File (dir ,texFile);
        dir.mkdir();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(str);
        writer.close();
    }

}
