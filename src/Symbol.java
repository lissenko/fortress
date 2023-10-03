/**
 * Symbol holds information about tokens in the source code.
 * It is returned by the lexer.
 */
public class Symbol{
	public static final int UNDEFINED_POSITION = -1;
	public static final Object NO_VALUE = null;

	private final LexicalUnit type;
	private Object value;
	private final int line,column;

	public Symbol(LexicalUnit unit,int line,int column,Object value){
    	this.type	= unit;
		this.line	= line+1;
		this.column	= column;
		this.value	= value;
	}

	public Symbol(LexicalUnit unit,int line,int column){
		this(unit,line,column,NO_VALUE);
	}

	public Symbol(LexicalUnit unit,int line){
		this(unit,line,UNDEFINED_POSITION,NO_VALUE);
	}

	public Symbol(LexicalUnit unit){
		this(unit,UNDEFINED_POSITION,UNDEFINED_POSITION,NO_VALUE);
	}

	public Symbol(LexicalUnit unit,Object value){
		this(unit,UNDEFINED_POSITION,UNDEFINED_POSITION,value);
	}

	public boolean isTerminal(){
		return this.type != null;
	}

	public boolean isNonTerminal(){
		return this.type == null;
	}

	public boolean hasValue() {
		if (this.value != null) {
			return true;
		}
		return false;
	}

	public LexicalUnit getType(){
		return this.type;
	}

	public Object getValue(){
		return this.value;
	}

	public int getLine(){
		return this.line;
	}

	public int getColumn(){
		return this.column;
	}

    public int getValueLength() {
        return this.value.toString().length();
    }

    public boolean isArith() {
        return this.value.toString().equals("+") || this.value.toString().equals("-") || this.value.toString().equals("*") || this.value.toString().equals("/");
    }

    public void modifyValue(String value) {
        this.value = value;
    }

    public void addMinus() {
        this.value = "-" + this.value;
    }

	@Override
	public int hashCode(){
		final String value	= this.value != null? this.value.toString() : "null";
		final String type		= this.type  != null? this.type.toString()  : "null";
		return new String(value+"_"+type).hashCode();
	}

	@Override
	public String toString(){
		if(this.isTerminal()){
			final String value	= this.value != null? this.value.toString() : "null";
			final String type		= this.type  != null? this.type.toString()  : "null";
      return String.format("token: %-15slexical unit: %s", value, type);
		}
		return "Non-terminal symbol";
	}
}
