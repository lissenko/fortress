/**
 * This class will simplify exprArith trees and generate the code for it.
 */
public class ExprArithHandler {

    private String exprArithCode = null;
    private boolean addMinus = false;

    /**
     * Pre order algorithm
     * Raise the operator when possible to build a simpler tree
     * @param tree: the current tree
     */
    private void raiseOperator(ParseTree tree) {
        if (tree.isExprArith() || tree.isProd() || tree.isExprArithPrime() || tree.isProdPrime()) {
            ParseTree prime = tree.get(1);
            if (!(prime.isLeaf() && prime.isVariable())) {
                ParseTree op = prime.get(0).get(0);
                tree.replaceLabel(op);
                prime.remove(0); // remove operator
            }
        }
        for (int i = 0; i < tree.numChildren(); ++i) {
            ParseTree child = tree.get(i);
            if (!(child.isLeaf() && child.isVariable())) {
                raiseOperator(child);
            } else {
                tree.remove(i--);
            }
        }
    }

    /**
     * Pre order algorithm
     * Browse the tree while another exprArith is found and replace the "toReplace" by it.
     * @param breach: the visitor layer
     * @param toReplace: the tree to replace
     * @param side: add to the right or the left ?
     */
    private void findNextArith(ParseTree breach, ParseTree toReplace, int side) {
        if (breach.getLabel().hasValue() && !breach.isParenthesis()) {
            if (breach.isLeaf() && breach.isMinus()) {
                addMinus = true;
            } else {
                if (addMinus) {
                    breach.getLabel().addMinus();
                    addMinus = false;
                }
                toReplace.setChild(side, breach);
                return;
            }
        }
        for (int i = 0; i < breach.numChildren(); ++i) {
            ParseTree child = breach.get(i);
            findNextArith(child, toReplace, side);
        }
    }

    /**
     * Fill the gap in the tree
     * Post order algorithm
     * @param tree: the current tree
     */
    private void postOrder(ParseTree tree) {
        for (int i = 0; i < tree.numChildren(); ++i) {
            ParseTree child = tree.get(i);
            postOrder(child);
        }
        if (!tree.isLeaf() && tree.isArithOperator()){
            ParseTree left = tree.get(0);
            ParseTree right = tree.get(1);
            findNextArith(left, tree, 0);
            findNextArith(right, tree, 1);
        } else if (!tree.isLeaf() && tree.isExprArith()) { // ExprArith standing alone
            if (tree.get(0).getLabel().hasValue()) {
                ParseTree interest = tree.get(0);
                tree.replaceLabel(interest);
                tree.setChildren(interest.getChildren());
            } else {
                findNextArith(tree, tree, 0);
                tree.replaceLabel(tree.get(0));
                tree.remove(0);
            }
        }
    }

    /**
     * Simplify the tree of the exprArith.
     * It will become a binary tree made of operators, variables and integers only. 
     * @param exprTree: the tree to simplify
     */
    public ParseTree simplifyExprArith(ParseTree exprTree) {
        raiseOperator(exprTree);
        postOrder(exprTree);
        return exprTree;
    }

    /* ------------------ Code Generation ------------------ */

    /**
     * Generate the code for the exprArith.
     * Post order algorithm
     * @param exprTree: the tree to generate the code
     */
    private void generateCodeFromExprArith(ParseTree exprTree) {
        for (int i = 0; i < exprTree.numChildren(); ++i) {
            ParseTree child = exprTree.get(i);
            generateCodeFromExprArith(child);
        }
        String code = new String();
        if (exprTree.isArithOperator()) {
            String op = exprTree.getString();
            String method = null;
            switch (op) {
                case "+":
                    method = "add";
                    break;
                case "-":
                    method = "sub";
                    break;
                case "*":
                    method = "mul";
                    break;
                case "/":
                    method = "sdiv";
                    break;
                default:
                    break;
            }
            code += "%"+ Counter.inc() + " = " + method + " i32 %" + exprTree.get(0).getString() + ", %" + exprTree.get(1).getString();
                
        } else {
            String symbol = exprTree.getString();
            // check symbol is a variable or a number
            try {
                Integer.parseInt(symbol);
                code += "%"+Counter.inc()+" = call i32 @assign(i32 " + symbol + ")";
            } catch (NumberFormatException e) {
                symbol = "%" + symbol;
                code += "%"+Counter.inc()+" = load i32, i32* " + symbol;
            }
        }
        exprArithCode += code + "\n";
        exprTree.modifyLabel(String.valueOf(Counter.n()));
    }

    /**
     * Correct the tree in case of this type of expression: 3 - 4 + 2.
     * The minus should be take into account when doing  the multiplication.
     * Preorder algorithm
     * @param exprTree: the tree to correct
     */
    private void correctTree(ParseTree exprTree) {
        if (exprTree.isMinus()) {
            ParseTree right = exprTree.get(1);
            if (!right.isLeaf()) {
                exprTree.modifyLabel("+");
                right.get(0).getLabel().addMinus();
            }
        } 
        for (int i = 0; i < exprTree.numChildren(); ++i) {
            ParseTree child = exprTree.get(i);
            correctTree(child);
        }
    }

    /**
     * Get the code for the exprArith.
     * @param exprTree: the tree to get the code
     */
    public String getExprArithCode(ParseTree exprTree) {
        exprArithCode = new String();
        correctTree(exprTree);
        generateCodeFromExprArith(exprTree);
        // cut the last \n
        exprArithCode = exprArithCode.substring(0, exprArithCode.length() - 1);
        return exprArithCode;
    }

}
