import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The class used to generate the llvm code from the AST
 */
public class LlvmGenerator {

    Instruction initialInstruction = null;

    /**
     * The constructor of the class
     * @param ast the AST to generate the llvm code from
     */
    public LlvmGenerator(Instruction instruction) {
        this.initialInstruction = instruction;
    }

    /**
     * The method used to include the generic llvm code.
     * @return the llvm code
     */
    public String loadLibrary() {
        BufferedReader reader;
        String content = "";
        try {
            reader = new BufferedReader(new FileReader("src/llvm-lib/lib.ll"));
            String line = reader.readLine();
            while (line != null) {
                content += line + "\n";
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * The method used to generate the llvm code from the AST
     * It is printed on stdout
     */
    public void generate() {
        System.out.println(loadLibrary());
        System.out.println("define i32 @main() {");
        System.out.println("entry:");

        for (Instruction instruction : this.initialInstruction.getThenList()) {
            System.out.println(instruction.generateCode());
        }
        System.out.println("ret i32 0");
        System.out.println("}");
    }
}

