
public class Symbol {
	private String value;
	
	public Symbol(String symbol) {
		this.value = symbol;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Symbol) {
			Symbol s = (Symbol) o;
			return this.value.equals(s.value);
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return this.value.hashCode();
	}
	
	public String toString() {
		return this.value;
	}
}
