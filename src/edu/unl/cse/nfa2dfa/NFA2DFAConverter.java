package edu.unl.cse.nfa2dfa;
import java.util.*;

import edu.unl.cse.utils.*;
/**
 * Convert NFA to DFA. 
 * @author Dongpu
 *
 */

public class NFA2DFAConverter {
	// Mapping between DFA to NFA states
	private Map<State, Set<State> > dfaStateHistory = new HashMap<State, Set<State> >(); 
	
	/*
	 * Convert NFA to DFA. 
	 */
	public State convert(State s){
		State result; 
		Set<State> startState = this.mergeEpsilonStates(s);  // Get DFA start state
		result = this.NFA2DFABuilder(startState);  // Build a DFA with given DFA start state
		result.setStart();
		
		// Assign id
		IdManager idManager = new IdManager(); 
		idManager.assignId(result); 
		
		// Set final states 
		for (State key : this.dfaStateHistory.keySet()) {
			for (State item : this.dfaStateHistory.get(key)) {
				if (item.isFinal()) {
					key.setFinal(); 
					break; 
				}
			}
		} 
		
		return result; 
	}
	
	/*
	 * For the given set of states, figure out ALL reachable states of a 
	 * transition and ALL reachable states of b transition. 
	 */
	private State NFA2DFABuilder(Set<State> nfaStates) { 
		if (nfaStates == null | nfaStates.size() == 0) {
			return null; 
		}
		
		// Check if already processed 
		for (State key : this.dfaStateHistory.keySet()) {
			if (this.compSet(this.dfaStateHistory.get(key), nfaStates)) {
				return null;  // Already processed, return
			}
		}
		
		State dfaState = new State();  // A dfa state
		this.dfaStateHistory.put(dfaState, nfaStates); 
		
		// a transition and b transition state
		Set<State> aSet = new HashSet<State>(); 
		Set<State> bSet = new HashSet<State>(); 
		
		// Find next states with a and b transition
		for (State item : nfaStates) {
			this.collectNextStates(item, aSet, bSet); 
		} 
		
		// Add next A transition
		if (aSet.size() != 0){
			boolean isBackEdgeA = false; 
			
			for (State key : this.dfaStateHistory.keySet()) {
				if (this.compSet(aSet, this.dfaStateHistory.get(key))) {
					isBackEdgeA = true; 
					dfaState.addTransition('a', key);  // Back edge
					break; 
				}
			}
			
			if (!isBackEdgeA) {
				dfaState.addTransition('a', this.NFA2DFABuilder(aSet));
			}
		}
		
		// Add next B transition 
		if (bSet.size() != 0) {
			boolean isBackEdgeB = false; 
			
			for (State key : this.dfaStateHistory.keySet()) {
				if (this.compSet(bSet, this.dfaStateHistory.get(key))) {
					isBackEdgeB = true; 
					dfaState.addTransition('b', key); // Back edge
					break; 
				}
			}
			
			if (!isBackEdgeB) {
				dfaState.addTransition('b', this.NFA2DFABuilder(bSet)); 
			}
		}
		
		return dfaState; 
	}
	
	/*
	 * Collect all reachable a and b states for start from the given state. 
	 */
	private void collectNextStates(State state, Set<State> aSet, Set<State> bSet) {
		if (state == null) {
			return; 
		}
		
		// Loop through next states of current state and collect a/b transitions
		for (Map<Symbol, State> item : state.getNext()) {
			Symbol key = item.keySet().toArray(new Symbol[0])[0]; 
			State value = item.get(key); 
			
			if (key.getType() == Symbol.SymbolEnum.A) {  // A transition
				aSet.addAll(this.mergeEpsilonStates(value)); 
			}
			else if (key.getType() == Symbol.SymbolEnum.B) {  // B transition 
				bSet.addAll(this.mergeEpsilonStates(value)); 
			}
		}
	}
	
	/*
	 * Merge epsilon states; 
	 */
	private Set<State> mergeEpsilonStates(State state) {
		Set<State> result = new HashSet<State>(); 
		if (state == null) {
			return result; 
		}
		
		Set<State> mergeEpsilonHistory = new HashSet<State>();
		
		return mergeEpsilonStatesHelper(state, mergeEpsilonHistory); 
	}
	
	private Set<State> mergeEpsilonStatesHelper(State state, Set<State> mergeEpsilonHistory) {
		Set<State> result = new HashSet<State>();
		if (state == null) {
			return result; 
		}
		
		if (mergeEpsilonHistory.contains(state)) {
			return result; 
		}
		
		mergeEpsilonHistory.add(state); 
		result.add(state); 
		
		for (Map<Symbol, State> item : state.getNext()) {
			Symbol key = item.keySet().toArray(new Symbol[0])[0]; 
			State value = item.get(key); 
			
			if (key.getType() == Symbol.SymbolEnum.EPSILON) {
				result.addAll(mergeEpsilonStatesHelper(value, mergeEpsilonHistory)); 
			}
		}
		
		return result; 
	}
	
	/*
	 * Check if two sets contain the same set of states. 
	 */
	private boolean compSet(Set<State> s1, Set<State> s2) {
		if (s1.containsAll(s2) && s2.containsAll(s1)) {
			return true; 
		}
		
		return false; 
	}
}
