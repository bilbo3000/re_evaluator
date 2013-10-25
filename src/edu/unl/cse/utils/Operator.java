package edu.unl.cse.utils;

/**
 * Enum for operators.
 * 
 * @author Dongpu
 * 
 */
public class Operator {
	public static enum OperatorEnum {
		STAR, CONCAT, UNION, INVALID
	}

	private OperatorEnum op;

	public Operator(char op) {
		if (op == '*') {
			this.op = OperatorEnum.STAR;
		} else if (op == '.') {
			this.op = OperatorEnum.CONCAT;
		} else if (op == '|') {
			this.op = OperatorEnum.UNION;
		} else {
			this.op = OperatorEnum.INVALID;
		}
	}

	public OperatorEnum getType() {
		return this.op;
	}
}
