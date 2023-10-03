import java.util.List;
import java.util.ArrayList;

/**
 * A skeleton class to represent parse trees. The arity is not fixed: a node can
 * have 0, 1 or more children. Trees are represented in the following way: Tree
 * :== GrammarUnit * List<Tree> In other words, trees are defined recursively: A tree
 * is a root (with a label of type GrammarUnit) and a list of trees children. Thus, a
 * leave is simply a tree with no children (its list of children is empty). This
 * class can also be seen as representing the Node of a tree, in which case a
 * tree is simply represented as its root.
 *
 * @author Léo Exibard, Sarah Winter
 */

public class ParseTree {
    private GrammarUnit label; // The label of the root of the tree
    private List<ParseTree> children; // Its children, which are trees themselves

    /**
     * Creates a singleton tree with only a root labeled by lbl.
     *
     * @param lbl The label of the root
     */
    public ParseTree(GrammarUnit lbl) {
        this.label = lbl;
        this.children = new ArrayList<ParseTree>(); // This tree has no children
    }

    /**
     * Creates a tree with root labeled by lbl and children chdn.
     *
     * @param lbl  The label of the root
     * @param chdn Its children
     */
    public ParseTree(GrammarUnit lbl, List<ParseTree> chdn) {
        this.label = lbl;
        this.children = chdn;
    }

    public GrammarUnit getLabel() {
        return this.label;
    }

    public boolean isInstruction() {
        return this.label.isVariable() && this.label.getVariable() == Variable.INSTRUCTION;
    }

    public boolean isCode() {
        return this.label.isVariable() && this.label.getVariable() == Variable.CODE;
    }

    public boolean isAtom() {
        return this.label.isVariable() && this.label.getVariable() == Variable.ATOM;
    }

    public boolean isExprArith() {
        return this.label.isVariable() && this.label.getVariable() == Variable.EXPRARITH;
    }

    public boolean isExprArithPrime() {
        return this.label.isVariable() && this.label.getVariable() == Variable.EXPRARITHPRIME;
    }

    public boolean isProd() {
        return this.label.isVariable() && this.label.getVariable() == Variable.PROD;
    }

    public boolean isIfSeq() {
        return this.label.isVariable() && this.label.getVariable() == Variable.IFSEQ;
    }

    public boolean isIf() {
        return this.label.isVariable() && this.label.getVariable() == Variable.IF;
    }

    public boolean isProdPrime() {
        return this.label.isVariable() && this.label.getVariable() == Variable.PRODPRIME;
    }

    public boolean isParenthesis() {
        return !this.label.isVariable() && this.label.getType() == LexicalUnit.LPAREN || this.label.getType() == LexicalUnit.RPAREN;
    }

    public boolean isMinus() {
        return !this.label.isVariable() && this.label.getType() == LexicalUnit.MINUS;
    }

    public boolean isArithOperator() {
        if (this.label.hasValue()) {
            return this.label.isArith();
        }
        return false;
    }

    public String getString() {
        return this.label.getString();
    }

    public List<ParseTree> getChildren() {
        return this.children;
    }

    public int numChildren() {
      return this.children.size();
    }

    public void setChildren(List<ParseTree> children) {
        this.children = children;
    }

    public void remove(int index) {
        this.children.remove(index);
    }

    public void addChildren(List<ParseTree> children) {
        for (ParseTree child : children) {
            this.children.add(child);
        }
    }

    public void add(ParseTree child) {
        this.children.add(child);
    }

    public void setLabel(GrammarUnit unit) {
        this.label = unit;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    public boolean isVariable() {
        return this.label.isVariable();
    }

    public void setChildren(GrammarUnit[] units) {
        ArrayList<ParseTree> list = new ArrayList<ParseTree>();
        for (int i = 0; i < units.length; ++i) {
            list.add(new ParseTree(units[i]));
        }
        this.children = list;
    }

    public ParseTree get(int idx) {
        return this.children.get(idx);
    }

    public void replaceLabel(ParseTree tree) {
        this.label = tree.label;
    }

    public void modifyLabel(String str) {
        this.label.modifyValue(str);
    }

    public void setChild(int idx, ParseTree tree) {
        this.children.set(idx, tree);
    }

    public ParseTree getLastChild() {
        return this.children.get(this.children.size() - 1);
    }

    public String toString() {
        return this.label.toString();
    }

    /**
     * Writes the tree as LaTeX code
     */
    public String toLaTexTree() {
        StringBuilder treeTeX = new StringBuilder();
        treeTeX.append("[");
        treeTeX.append("{" + label.toTexString() + "}");
        treeTeX.append(" ");

        for (ParseTree child : children) {
            treeTeX.append(child.toLaTexTree());
        }
        treeTeX.append("]");
        return treeTeX.toString();
    }

    /**
     * Writes the tree as TikZ code. TikZ is a language to specify drawings in LaTeX
     * files.
     */
    public String toTikZ() {
        StringBuilder treeTikZ = new StringBuilder();
        treeTikZ.append("node {");
        treeTikZ.append(label.toTexString());  // Implement this yourself in GrammarUnit.java
        treeTikZ.append("}\n");
        for (ParseTree child : children) {
            treeTikZ.append("child { ");
            treeTikZ.append(child.toTikZ());
            treeTikZ.append(" }\n");
        }
        return treeTikZ.toString();
    }

    /**
     * Writes the tree as a TikZ picture. A TikZ picture embeds TikZ code so that
     * LaTeX undertands it.
     */
    public String toTikZPicture() {
        return "\\begin{tikzpicture}[tree layout]\n\\" + toTikZ() + ";\n\\end{tikzpicture}";
    }


    /**
     * Writes the tree as a forest picture. Returns the tree in forest enviroment
     * using the latex code of the tree
     */
    public String toForestPicture() {
        return "\\begin{forest}for tree={rectangle, draw, l sep=20pt}" + toLaTexTree() + ";\n\\end{forest}";
    }

    /**
     * Writes the tree as a LaTeX document which can be compiled using PDFLaTeX.
     * <br>
     * <br>
     * The result can be used with the command:
     *
     * <pre>
     * pdflatex some-file.tex
     * </pre>
     */
    public String toLaTeX() {
        return "\\documentclass[border=5pt]{standalone}\n\n\\usepackage{tikz}\n\\usepackage{forest}\n\n\\begin{document}\n\n"
                + toForestPicture() + "\n\n\\end{document}\n%% Local Variables:\n%% TeX-engine: pdflatex\n%% End:";
    }
}
