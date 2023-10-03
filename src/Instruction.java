import java.util.List;
import java.util.ArrayList;

/**
 * This class represents an instruction in the AST.
 * It is the base class for all instructions.
 */
public class Instruction {
    protected ParseTree tree;
    protected List<Instruction> children; // Its children, which are trees themselves
    protected List<Instruction> elsechildren;
    // common to all Instruction instances
    static protected ExprArithHandler exprArithHandler = new ExprArithHandler();
    // symbol table - to not define a variable twice
    static protected List<String> symbolTable = new ArrayList<String>();

    /**
     * Constructor.
     * @param tree: the tree to be represented by this instruction
     */
    public Instruction(ParseTree tree) {
        this.tree = tree;
        this.children = new ArrayList<Instruction>();
        this.elsechildren = new ArrayList<Instruction>();
    }

    public Instruction getThen(int idx) {
        return children.get(idx);
    }

    public Instruction getElse(int idx) {
        return elsechildren.get(idx);
    }

    public List<Instruction> getThenList() {
        return children;
    }

    public List<Instruction> getElseList() {
        return elsechildren;
    }


    public ParseTree getTree() {
        return tree;
    }

    /**
     * Generate the code for this instruction.
     */
    public String generateCode() {
        return "";
    }

    /**
     * Simplify the tree for this instruction.
     */
    public void simplify() {}


    /**
     * Assign the value of an expression to a variable.
     * @param var: the variable to assign
     * @param expr: the expression to evaluate
     */
    protected String assign(String varname, ParseTree exprTree) {
        String code = exprArithHandler.getExprArithCode(exprTree) + "\n";
        if (!symbolTable.contains(varname)) {
            code += "%" + varname + " = alloca i32\n";
            symbolTable.add(varname);
        }
        code += "store i32 %" + Counter.n() + ", i32* %" + varname;
        return code;
    }

}

/**
 * This class represents an assign instruction in the AST.
 */
class Condition extends Instruction {

    protected static int labelId = 0;
    protected String comparator = "";
    protected int lastNLeft = 0;
    protected int lastNRight = 0;

    /**
     * Constructor.
     * @param tree: the tree to be represented by this instruction
     */
    public Condition(ParseTree tree) {
        super(tree);
    }

    public void simplify() {
        ParseTree cond = tree.get(2); // condition
        tree = tree.get(2).get(1).get(0).get(0); // comp symbol
        ParseTree leftExprArith = exprArithHandler.simplifyExprArith(cond.get(0));
        ParseTree rightExprArith = exprArithHandler.simplifyExprArith(cond.get(2));
        tree.add(leftExprArith);
        tree.add(rightExprArith);
    }

    public String generateCode() {
        String code = new String();
        code += exprArithHandler.getExprArithCode(tree.get(0)) + "\n";
        this.lastNLeft = Counter.n();
        code += exprArithHandler.getExprArithCode(tree.get(1)) + "\n";
        this.lastNRight = Counter.n();
        String operator = tree.getString();
        switch (operator) {
            case "=":
            this.comparator = "eq";
            break;
            case "<":
            this.comparator = "slt";
            break;
            case ">":
            this.comparator = "sgt";
            break;
        }
        return code;

    }

}

class SimpleInstruction extends Instruction {
    public SimpleInstruction(ParseTree tree) {
        super(tree);
    }

    public void simplify() {
        tree = tree.get(2).get(0); // get the varname
    }
}

class If extends Condition {

    public If(ParseTree tree) {
        super(tree);
    }

    public String generateCode() {
        String code = new String();
        int id = labelId++;
        code += super.generateCode();
        code += "%cond_" + id + " = icmp "+ this.comparator + " i32 %" + this.lastNLeft + ", %" + this.lastNRight + "\n";
        code += "br i1 %cond_" + id + ", label %then_" + id + ", label %else_" + id + "\n";
        code += "then_" + id + ":\n";
        for (Instruction child : children) {
            code += child.generateCode() + "\n";
        }
        code += "br label %end_" + id + "\n";
        code += "else_" + id + ":\n";
        for (Instruction child : elsechildren) {
            code += child.generateCode() + "\n";
        }
        code += "br label %end_" + id + "\n";
        code += "end_" + id + ":";
        return code;
    }

}

class While extends Condition {

    public While(ParseTree tree) {
        super(tree);
    }

    public String generateCode() {
        String code = new String();
        int id = labelId++;
        code += "br label %eval_cond_" + id + "\n";
        code += "eval_cond_" + id + ":\n";
        code += super.generateCode();
        code += "%cond_" + id + " = icmp "+ this.comparator + " i32 %" + this.lastNLeft + ", %" + this.lastNRight + "\n";
        code += "br i1 %cond_" + id + ", label %do_" + id + ", label %end_" + id + "\n";
        code += "do_" + id + ":\n";
        for (Instruction child : children) {
            code += child.generateCode() + "\n";
        }
        code += "br label %eval_cond_" + id + "\n";
        code += "end_" + id + ":";
        return code;
    }
}

class Print extends SimpleInstruction {

    public Print(ParseTree tree) {
        super(tree);
    }

    public String generateCode() {
        String varname = tree.getString();
        String code = "%"+Counter.inc()+" = load i32, i32* %" + varname + "\n";
        code += "call void @println(i32 %" + Counter.n() + ")\n";
        return code;
    }

}

class Read extends SimpleInstruction {

    public Read(ParseTree tree) {
        super(tree);
    }

    public String generateCode() {
        String varname = tree.getString();
        String code = "%"+Counter.inc()+" = call i32 @readInt()\n";
        if (!symbolTable.contains(varname)) {
            code += "%" + varname + " = alloca i32\n";
            symbolTable.add(varname);
        }
        code += "store i32 %" + Counter.n() + ", i32* %" + varname;
        return code;
    }
}

class Assign extends Instruction {

    public Assign(ParseTree tree) {
        super(tree);
    }

    public void simplify() {
        ParseTree name = tree.get(0).get(0);
        ParseTree expr = exprArithHandler.simplifyExprArith(tree.get(2));
        tree = tree.get(1).get(0); // := symbol
        tree.add(name);
        tree.add(expr);
    }

    public String generateCode() {
        String varname = tree.get(0).getString();
        ParseTree expr = tree.get(1);
        return assign(varname, expr);
    }

}
