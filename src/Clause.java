import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A clause is a disjunction of literals
 */
public class Clause {
	
	private List<Literal> literals;
	private List<Symbol> symbols;
	
	public Clause(List<Literal> literals) {
		this.literals = new ArrayList<Literal>(literals);
		this.symbols = new ArrayList<Symbol>();
		
		HashMap<String,Boolean> existing = new HashMap<String,Boolean>();
		for(Literal l : this.literals){
			if(!existing.containsKey(l.getSymbol())){
				existing.put(l.getSymbol(), true);
				this.symbols.add(new Symbol(l.getSymbol()));
			}
		}
	}

	public List<Literal> getLiterals() {
		return literals;
	}
	
	public List<Symbol> getSymbols(){
		return symbols;
	}

	public void setLiterals(List<Literal> literals) {
		this.literals = literals;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Clause) {
			Clause c = (Clause) o;
			return literals.containsAll(c.literals) &&
				c.literals.containsAll(literals);
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		int result = 0;
		for (Literal l : literals) {
			result += l.hashCode();
		}
		return result;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < literals.size(); i++) {
			sb.append(literals.get(i).toString());
			if (i + 1 < literals.size()) {
				sb.append(" || ");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
}
