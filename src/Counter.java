/**
 * Instruction counter for the llvm code.
 */
public class Counter {

    static int count = -1;

    /**
     * Get n and increment it
     * @return the current value of n
     */
    static public int inc() {
        return ++count;
    }

    /**
     * Get n
     * @return the current value of n
     */
    static public int n() {
        return count;
    }

}
