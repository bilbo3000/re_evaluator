package edu.unl.cse.utils;
import java.util.*;

/**
 * Represent each finite automata state with transition
 * as an edge. 
 * 
 * @author Dongpu
 *
 */
public class State {
	private int id;  // id of current state
	private Set<Map<Symbol, State> > next = new HashSet<Map<Symbol, State> >(); // A list of next state
	private boolean isStart; 
	private boolean isFinal;  
	
	public State() {
		this.id = 0; 
		this.isStart = false; 
		this.isFinal = false; 
	}
	
	public State(int id) {
		this.id = id; 
		this.isStart = false; 
		this.isFinal = false; 
	}
	
	public int getId(){
		return this.id; 
	}
	
	public void setId(int id) {
		this.id = id; 
	}
	
	public void setStart() {
		this.isStart = true; 
	}
	
	public void clearStart() {
		this.isStart = false; 
	}
	
	public boolean isStart() {
		return this.isStart; 
	}
	
	public void setFinal() {
		this.isFinal = true; 
	}
	
	public void clearFinal() {
		this.isFinal = false; 
	}
	
	public boolean isFinal() {
		return this.isFinal; 
	}
	
	public Set<Map<Symbol, State> > getNext() {
		return this.next; 
	}
	
	/*
	 * Add a new transition to this state. 
	 */
	public void addTransition(char c, State state) {
		Symbol symbol = new Symbol(c); 
		
		if(symbol.getType() == Symbol.SymbolEnum.INVALID) {
			return; 
		}
		
		Map<Symbol, State> pair = new HashMap<Symbol, State>(); 
		pair.put(symbol, state); 
		this.next.add(pair); 
	}
	
	/*
	 * Print the automata. Used for debugging. 
	 */
	public void print(){
		Set<State> printHistory = new HashSet<State>();
		printHelper(this, printHistory); 
		System.out.println("----"); 
	}
	
	void printHelper(State s, Set<State> printHistory) {
		if (printHistory.contains(s)) {
			return; 
		}

		printHistory.add(s); 
		
		if (s.getNext().size() != 0) {
			// Print current node 
			System.out.print("Node " + s.getId()); 
			
			if (s.isStart()) {
				System.out.print("[Start]"); 
			}
			
			if (s.isFinal()) {
				System.out.print("[Final]"); 
			}
			
			System.out.println("-->"); 
			
			// Print next states 
			for (Map<Symbol, State> item : s.getNext()){
				Symbol key = item.keySet().toArray(new Symbol[0])[0]; 
				System.out.println("\t" + item.get(key).getId() + " on " + key.getType().toString() + " edge. ");
			}
			
			// Recursively process next states 
			for (Map<Symbol, State> item : s.getNext()){
				Symbol key = item.keySet().toArray(new Symbol[0])[0]; 
				printHelper(item.get(key), printHistory); 
			}
		}
	}
}
