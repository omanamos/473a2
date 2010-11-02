import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.StringTokenizer;


public class Main {
	/**
	 * Generate a random instance of 3SAT
	 * @param m the number of clauses
	 * @param n the number of variables
	 * @return the random 3SAT
	 */
	public static KnowledgeBase random3SAT(int m, int n) {
		Random r = new Random();
		ArrayList<Clause> clauses = new ArrayList<Clause>();
		for (int i = 0; i < m; i++) {
			Literal a = new Literal(r.nextInt(n) + "", r.nextBoolean());
			Literal b = new Literal(r.nextInt(n) + "", r.nextBoolean());
			Literal c = new Literal(r.nextInt(n) + "", r.nextBoolean());
			clauses.add(new Clause(Arrays.asList(new Literal[] {a, b, c})));
		}
		return new KnowledgeBase(clauses);
	}
	
	/**
	 * Read in a knowledge base in CNF form
	 * @param filename the file to read from
	 * @return the knowledge base
	 */
	public static KnowledgeBase readCNF(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = null;
			ArrayList<Clause> clauses = new ArrayList<Clause>();
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				ArrayList<Literal> literals = new ArrayList<Literal>();
				if (!st.hasMoreTokens()) {
					continue;
				}
				while (st.hasMoreTokens()) {
					String s =  st.nextToken();
					boolean negated = false;
					if (s.startsWith("!")) {
						s = s.substring(1);
						negated = true;
					}
					literals.add(new Literal(s, negated));
				}
				clauses.add(new Clause(literals));
			}
			br.close();
			return new KnowledgeBase(clauses);
		} catch (FileNotFoundException e) {
			System.out.println(e);
			return null;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	/**
	 * Write out a knowledge base in CNF form
	 * @param filename the file to write to
	 * @param kb the knowledge base to write
	 * @return true if successful
	 */
	public static boolean writeCNF(String filename, KnowledgeBase kb) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(filename));
			for (Clause c : kb.getClauses()) {
				for (Literal l : c.getLiterals()) {
					if (l.isNegated()) {
						pw.print("!");
					}
					pw.print(l.getSymbol());
					pw.print(" ");
				}
				pw.println();
			}
			pw.close();
			return true;
		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * Code to expand a a single clause by replacing
	 * variables with ground terms
	 * @param c the clause to expand
	 * @param variables the list of variables to eliminate
	 * @param groundSet the list of ground terms to replace variables with
	 * @param clauses the list of clauses to which we should add expanded clauses
	 */
	private static void expandClauseRecursive(Clause c,
			List<String> variables, List<String> groundSet,
			List<Clause> clauses) {
		if (variables.size() == 0) {
			c = new Clause(c.getLiterals());
			if (!clauses.contains(c)) {
				clauses.add(c);
			}
			return;		}
		String variable = variables.remove(variables.size()-1);
		boolean containsVariable = false;
		for (Literal l : c.getLiterals()) {
			if (l.getSymbol().indexOf(variable) != -1) {
				containsVariable = true;
			}
		}
		if (containsVariable) {
			for (String groundTerm : groundSet) {
				List<Literal> newLiterals = new ArrayList<Literal>();
				for (Literal l : c.getLiterals()) {
					String newSymbol = l.getSymbol().replaceAll(variable, groundTerm);
					newLiterals.add(new Literal(newSymbol, l.isNegated()));
				}
				Clause newClause = new Clause(newLiterals);
				expandClauseRecursive(newClause, variables, groundSet, clauses);
			}
		} else {
			expandClauseRecursive(c, variables, groundSet, clauses);
		}
		variables.add(variable);
	}
	
	/**
	 * Code to expand a knowledge base by replacing variables
	 * with ground terms
	 * @param kb the knowledge base to expand
	 * @param variables list of variables (i.e. X1, X2, X3)
	 * @param groundSet list of groundterms (i.e. Frank, Tom)
	 * @return the expanded knowledge base
	 */
	public static KnowledgeBase expandKnowledgeBase(KnowledgeBase kb, 
			List<String> variables, List<String> groundSet) {
		ArrayList<Clause> clauses = new ArrayList<Clause>();
		for (Clause c : kb.getClauses()) {
			expandClauseRecursive(c, variables, groundSet, clauses);
		}
		return new KnowledgeBase(clauses);
	}
	
	/**
	 * Code to read in a knowledge base from "firstorder.txt", 
	 * propositionalize, then write back out as "knowledgebase.txt"
	 * Note: the variable names and ground term names are hardcoded in here
	 * This code assumes that your variables are named X1, X2, X3, and X4
	 * and the ground terms are Frank, Bob, Jill, Mary, Tom.
	 * This code also assumes that variables are uniquely named (it replaces
	 * variables with simple string find-replace).  So, if
	 * you use variable names like "a" or "b" this will likely result in garbage output
	 * (it will replace all substrings that match "a" and "b").
	 */
	public static void propositionalize() {
		KnowledgeBase kb = readCNF("firstorder.txt");
		KnowledgeBase kbExpanded = expandKnowledgeBase(kb,
			new ArrayList<String>(Arrays.asList(new String[] { "X1", "X2", "X3", "X4",})),
			new ArrayList<String>(Arrays.asList(new String[] { "Frank", "Bob", "Jill", "Mary", "Tom" })));
		System.out.println(writeCNF("knowledgebase.txt", kbExpanded));
	}
	
	public static void main(String[] args) {
		//propositionalize();
		KnowledgeBase kb = readCNF("knowledgebase.txt");
		System.out.println(kb.walkSAT(0.5, 10000));
	}
}
