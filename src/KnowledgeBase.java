import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A knowledge base in CNF form
 */
public class KnowledgeBase {
	/**
	 * The list of clauses
	 */
	private List<Clause> clauses;
	
	public KnowledgeBase(List<Clause> clauses) {
		this.clauses = new ArrayList<Clause>(clauses);
	}

	public List<Clause> getClauses() {
		return clauses;
	}

	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
	}
	
	public boolean equals(Object o) {
		if (o instanceof KnowledgeBase) {
			KnowledgeBase kb = (KnowledgeBase) o;
			return clauses.containsAll(kb.clauses) && 
				kb.clauses.containsAll(clauses);
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		int result = 0;
		for (Clause c : clauses) {
			result += c.hashCode();
		}
		return result;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < clauses.size(); i++) {
			sb.append(clauses.get(i).toString());
			if (i + 1 < clauses.size()) {
				sb.append(" && ");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	public boolean walkSAT(double p, int maxFlips) {
		Map<Symbol,Boolean> model = this.randomAssignment();
		
		for(int i = 0; i < maxFlips; i++){
			ArrayList<ArrayList<Clause>> tmp = this.partitionClauses(model);
			ArrayList<Clause> unsat = tmp.get(1);
			if(unsat.size() == 0)
				return true;
			Clause c = unsat.get((int)(Math.random() * unsat.size()));
			if(Math.random() > p){
				Symbol s = new Symbol(c.getLiterals().get((int)(Math.random() * c.getLiterals().size())).getSymbol());
				Boolean curVal = model.get(s);
				model.put(s, !curVal);
			}else{
				model = this.maximizeOutcome(c, model);
			}
		}
		return false;
	}
	
	private Map<Symbol,Boolean> maximizeOutcome(Clause c, Map<Symbol,Boolean> model){
		List<Symbol> symbols = c.getSymbols();
		int[] cnts = new int[symbols.size()];
		for(int i = 0; i < cnts.length; i++){
			Symbol sym = symbols.get(i);
			Map<Symbol,Boolean> tmp = new HashMap<Symbol,Boolean>(model);
			model.put(sym, !model.get(sym));
			cnts[i] = countSatisfied(tmp);
		}
		int maxInd = max(cnts);
		Symbol optimalSymbol = symbols.get(maxInd);
		Boolean curValue = model.get(optimalSymbol);
		model.put(optimalSymbol, !curValue);
		return model;
	}
	
	private static int max(int[] arr){
		int max = arr[0];
		int ind = 0;
		for(int i = 0; i < arr.length; i++){
			if(max < arr[i]){
				max = arr[i];
				ind = i;
			}
		}
		return ind;
	}
	
	private int countSatisfied(Map<Symbol,Boolean> model){
		return partitionClauses(model).get(0).size();
	}
	
	private Map<Symbol,Boolean> randomAssignment(){
		HashMap<Symbol,Boolean> rtn = new HashMap<Symbol,Boolean>();
		for(Clause c : this.clauses){
			for(Symbol s : c.getSymbols()){
				if(!rtn.containsKey(s)){
					rtn.put(s, Math.round(Math.random()) == 1);
				}
			}
		}
		return rtn;
	}

	private ArrayList<ArrayList<Clause>> partitionClauses(Map<Symbol,Boolean> model){
		ArrayList<Clause> sat = new ArrayList<Clause>();
		ArrayList<Clause> unsat = new ArrayList<Clause>();
		
		for(Clause c : this.clauses){
			boolean satisfied = false;
			for(Literal l : c.getLiterals()){
				if(l.isNegated() ^ model.get(new Symbol(l.getSymbol()))){
					sat.add(c);
					satisfied = true;
					break;
				}
			}
			if(!satisfied) unsat.add(c);
		}
		ArrayList<ArrayList<Clause>> rtn = new ArrayList<ArrayList<Clause>>();
		rtn.add(sat);
		rtn.add(unsat);
		return rtn;
	}
}
