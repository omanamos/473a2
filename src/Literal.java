/**
 * A literal is a symbol which is or is not negated
 * i.e. StuckInPit or !StuckInPit
 */
public class Literal {
	private String symbol;
	private boolean negated;
	
	public Literal(String symbol, boolean negated) {
		this.symbol = symbol;
		this.negated = negated;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public boolean isNegated() {
		return negated;
	}	
	public boolean equals(Object o) {
		if (o instanceof Literal) {
			Literal l = (Literal) o;
			return symbol.equals(l.symbol) &&
				(negated == l.negated);
		} else {
			return false;
		}
	}	
	public int hashCode() {
		return symbol.hashCode() + (negated ? 1 : 0);
	}
	
	public String toString() {
		if (negated) {
			return "!" + symbol;
		} else {
			return symbol;
		}
	}
}
