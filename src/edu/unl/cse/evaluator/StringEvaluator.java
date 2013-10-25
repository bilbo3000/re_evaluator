package edu.unl.cse.evaluator;

import edu.unl.cse.utils.State;
import edu.unl.cse.utils.Symbol;
import java.util.*;

/**
 * Evaluate given string against given DFA model.
 * 
 * @author Dongpu
 * 
 */
public class StringEvaluator {
	/*
	 * Evaluate the given string.
	 */
	public boolean evaluate(String s, State dfa) {
		if (s.length() == 0) { // Empty string
			return false;
		}

		return evaluateHelper(s, 0, dfa);
	}

	private boolean evaluateHelper(String s, int currIndex, State currState) {
		if (currIndex == s.length()) {
			// Reach final states when finish processing all chars
			if (currState.isFinal()) {
				return true;
			} else {
				return false;
			}
		}

		Symbol symbol = new Symbol(s.charAt(currIndex));

		// Skip epsilon string
		if (symbol.getType() == Symbol.SymbolEnum.EPSILON) {
			return evaluateHelper(s, currIndex + 1, currState);
		}

		for (Map<Symbol, State> next : currState.getNext()) {
			Symbol key = next.keySet().toArray(new Symbol[0])[0];
			State value = next.get(key);

			if (symbol.getType() == key.getType()) {
				return evaluateHelper(s, currIndex + 1, value);
			}
		}

		return false;
	}
}
