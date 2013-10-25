package edu.unl.cse.postfix2nfa;
import java.util.*;

import edu.unl.cse.utils.IdManager;
import edu.unl.cse.utils.Operator;
import edu.unl.cse.utils.State;
import edu.unl.cse.utils.Symbol;

/**
 * Convert postfix notation to NFA. 
 * @author Dongpu
 *
 */
public class Postfix2NFAConverter {

	public State convert(String postfixRE) {
		Stack<State> s = new Stack<State>(); 
		char[] postfixREChar = postfixRE.toCharArray(); 
		
		for (int i = 0; i < postfixREChar.length; i++) {
			Symbol symbol = new Symbol(postfixREChar[i]);
			Operator operator = new Operator(postfixREChar[i]); 
			
			if (symbol.getType() != Symbol.SymbolEnum.INVALID) {  // A symbol
				s.push(this.buildSymbolNFA(postfixREChar[i]));  // Push corresponding NFA onto stack 
			}
			else if (operator.getType() != Operator.OperatorEnum.INVALID) {  // An operator
				if (operator.getType() == Operator.OperatorEnum.UNION) {  // | operator
					State s2 = s.pop(); 
					State s1 = s.pop(); 
					State result = this.buildOrNFA(s1, s2); 
					s.push(result); 
				}
				else if (operator.getType() == Operator.OperatorEnum.CONCAT){  // . operator
					State s2 = s.pop(); 
					State s1 = s.pop(); 
					State result = this.buildConcatNFA(s1, s2); 
					s.push(result); 
				}
				else if (operator.getType() == Operator.OperatorEnum.STAR) {  // * operator
					State s1 = s.pop(); 
					State result = this.buildStarNFA(s1); 
					s.push(result); 
				}
			}
		}
		
		return s.pop(); 
	}
	
	/*
	 * Build a NFA for a single symbol. 
	 */
	private State buildSymbolNFA(char c) {
		State startState = new State(0);
		startState.setStart(); 
		
		State finalState = new State(1); 
		finalState.setFinal(); 
		
		startState.addTransition(c, finalState); 
		
		return startState; 
	}
	
	/*
	 * Build a NFA that is the OR of two NFA. 
	 */
	private State buildOrNFA(State s1, State s2){
		if (s2 == null) {
			return s1; 
		}
		
		if (s1 == null) {
			return s2; 
		}
		
		State startState = new State(0);
		startState.setStart(); 
		s1.clearStart(); 
		s2.clearStart(); 
		
		startState.addTransition('e', s1); 
		startState.addTransition('e', s2); 

		// Re-assign state id. 
		IdManager idManager = new IdManager(); 
		idManager.assignId(startState); 
		
		return startState; 
	}
	
	/*
	 * Build a NFA that is the CONCAT of two NFA. 
	 */
	private State buildConcatNFA(State s1, State s2) {
		// Clear final states in s1 and start state in s2
		Set<State> clearFinalStatesHistory = new HashSet<State>();  // Traverse history
		Set<State> finalStates = new HashSet<State>();   // All the final states in s1
		clearFinalStates(s1, clearFinalStatesHistory, finalStates); 
		s2.clearStart(); 
		
		// Connects finals states in s1 to the start state of s2 
		for (State finalState : finalStates) {
			finalState.addTransition('e', s2); 
		}
		
		// Assign id
		IdManager idManager = new IdManager(); 
		idManager.assignId(s1); 
		
		return s1; 
	}
	
	/*
	 * Clear all the final states of a given automata. 
	 */
	private void clearFinalStates(State s, Set<State> clearFinalStatesHistory, Set<State> finalStates){
		if (s == null) {
			return; 
		}
		
		if (clearFinalStatesHistory.contains(s)){ // Do not visit state that has been visited
			return; 
		}
		
		clearFinalStatesHistory.add(s);  // Add to visit history
		
		if (s.isFinal()) {  // Clear final state
			s.clearFinal(); 
			finalStates.add(s); 
		}
		
		// DFS 
		for (Map<Symbol, State> item : s.getNext()){
			Symbol key = item.keySet().toArray(new Symbol[0])[0]; 
			State value = item.get(key); 
			clearFinalStates(value, clearFinalStatesHistory, finalStates); 
		}
	}
	
	/*
	 * Build a NFA that is the star of an NFA. 
	 */
	private State buildStarNFA(State s1) { 
		State startState = new State(0);  // New start and final state
		startState.setStart(); 
		startState.setFinal(); 
		s1.clearStart();  
		
		startState.addTransition('e', s1); // New epsilon transition
		
		// Connect all final states to the start state
		Set<State> stateHistory = new HashSet<State>(); 
		buildStarNFAHelper(s1, s1, stateHistory); 
		
		// Assign ids 
		IdManager idManager = new IdManager(); 
		idManager.assignId(startState); 
		
		return startState; 
	}
	
	/*
	 * Connect final states to a given state.
	 */
	void buildStarNFAHelper(State curr, State target, Set<State> stateHistory){
		if (curr == null || target == null) {
			return; 
		}
		
		if (stateHistory.contains(curr)) {  // Do not visit state that has been visited
			return; 
		}
		
		stateHistory.add(curr); 
		
		// Connect final state to start state 
		if (curr.isFinal()) {
			curr.addTransition('e', target); 
		}
		
		// DFS
		for (Map<Symbol, State> item : curr.getNext()) {
			Symbol key = item.keySet().toArray(new Symbol[0])[0];
			State value = item.get(key); 
			buildStarNFAHelper(value, target, stateHistory); 
		}
	}
}
