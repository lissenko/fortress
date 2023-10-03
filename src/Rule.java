/**
 * A rule has a right and a left side.
 * Each side is a list of grammar units.
 */
public class Rule {

    static int count = 0;
    private GrammarUnit leftHandSide = null;
    private GrammarUnit[] rightHandSide = null;
    private int idx = 0;

    public Rule(GrammarUnit leftHandSide, GrammarUnit[] rightHandSide) {
        this.idx = count++;
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    public int idx() {
        return idx;
    }

    public String toString() {
        String s = Integer.toString(this.idx + 1) + " ";
        return s;
    }

    public GrammarUnit[] getRight() {
        return this.rightHandSide;
    }

    public GrammarUnit getLeftHandSide() {
        return this.leftHandSide;
    }

    public boolean isEpsilon() {
        return rightHandSide.length == 1 && rightHandSide[0].getType() == LexicalUnit.EPSILON;
    }



}
