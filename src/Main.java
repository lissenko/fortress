import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.IOException;

/**
 * Implementation of a Lexical Analyzer for the Fortress Language.
 */
public class Main {

    public static void main(String[] args) {
        String encodingName = "UTF-8";
        String inputFile = null;
        String texFile = null;
        boolean outputTree = args[0].equals("-wt");
        if (outputTree) {
            inputFile = args[2];
            texFile = args[1];
        } else {
            inputFile = args[0];
        }
        try {
            FileInputStream stream = new FileInputStream(inputFile);
            Reader reader = new InputStreamReader(stream, encodingName);
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(lexer);
            parser.parse();
            if (outputTree) {
                parser.createTreeFile(texFile);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found : \"" + inputFile + "\"");
        } catch (IOException e) {
            System.out.println("IO error scanning file \"" + inputFile + "\"");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println("Unexpected exception:");
            e.printStackTrace();
        }

    }
}
