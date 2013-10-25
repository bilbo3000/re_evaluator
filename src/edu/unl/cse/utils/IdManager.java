package edu.unl.cse.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Assign Id to given finite automata. 
 * @author Dongpu
 *
 */
public class IdManager {
	private Set<State> assignIdHistory = new HashSet<State>(); 
	private int idCount = 0; // id counter for assign ids.
	
	/*
	 * Assign id for a NFA. 
	 */
	public void assignId(State s) {
		if (s == null) {
			return; 
		}
		
		if (assignIdHistory.contains(s)) { // Do not visit state that has been visited
			return; 
		}
		
		s.setId(idCount); 
		idCount++; 
		this.assignIdHistory.add(s); 
		
		// DFS
		for (Map<Symbol, State> item : s.getNext()) {
			Symbol key = item.keySet().toArray(new Symbol[0])[0];
			State value = item.get(key); 
			assignId(value); 
		}
	}
}
