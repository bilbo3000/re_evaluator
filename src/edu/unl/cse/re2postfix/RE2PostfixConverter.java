package edu.unl.cse.re2postfix;



import java.util.Stack;
import java.util.ArrayList;

import edu.unl.cse.utils.Operator;


public class RE2PostfixConverter {
	Stack stack = new Stack();
	ArrayList output = new ArrayList();
	
	/**
	 * Insert concat operator into the original infix RE. 
	 * 
	 * @param infixRE
	 * @return
	 */
	private String insertConcat(String infixRE) {
		if (infixRE.length() <= 1) {
			return infixRE; 
		}
		
		char[] infixREArr = infixRE.toCharArray(); 
		String result = "" + infixREArr[0]; 
		char prev = infixREArr[0]; 
		
		for (int i = 1; i < infixREArr.length; i++) {
			char curr = infixREArr[i]; 
			
			if (!(curr == '|' || curr == '*' || curr == ')') && 
					!(prev == '(' || prev == '|')) {
				result += "."; 
			}
			
			result += curr; 
			prev = curr; 
		}
		
		return result; 
	}

	/**
	 * Convert infix RE notation to postfix notation. 
	 * 
	 * @param infixRE
	 * @return
	 */
	public String convert(String infixRE) {
		if (infixRE == null) {
			return null;
		}
		
		infixRE = this.insertConcat(infixRE); 

		for (char ch : infixRE.toCharArray()) {
			switch (ch) {
			case 'a':
			case 'b':
			case 'e': 
				output.add(ch);
				break;
			case '(':
				stack.push(ch);
				break;
			case ')':
				while (stack.size() != 0 && (char) stack.peek() != '(') {
					output.add(stack.pop());
				}

				if ((char) stack.peek() == '(') {
					stack.pop();
				}

				break;
			case '*':
				output.add(ch);
				break; 
			case '.': 
			case '|':
				if (stack.size() == 0) { // Stack is empty
					stack.push(ch); 
				}
				else {
					Operator op = new Operator(ch); 
					Operator tos = new Operator((char)(stack.peek()));  // Top of the stack
					
					Operator.OperatorEnum tosType = tos.getType(); 
					
					if (tosType != Operator.OperatorEnum.INVALID) {  // TOS is operator
						if (tosType.compareTo(op.getType()) > 0) {  // TOS has lower precedence
							stack.push(ch); 
						}
						else {  // TOS has higher precedence
							output.add(stack.pop());
							stack.add(ch); 
						}
					}
					else {
						stack.push(ch); 
					}
				}
				break; 
			default:
				break;
			}
		}
		
		while (stack.size() != 0) {
			output.add(stack.pop()); 
		}

		String postfixRE = ""; 
		
		for (int i = 0; i < output.size(); i++) {
			postfixRE += output.get(i); 
		}
		
		return postfixRE; 
	}
}
