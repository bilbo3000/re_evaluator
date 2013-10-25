package edu.unl.cse.utils;
/**
 * Class that represent each symbol. 
 * @author Dongpu
 *
 */

public class Symbol {
	public static enum SymbolEnum{
		A, 
		B, 
		EPSILON, 
		INVALID
	}; 
	
	private SymbolEnum symbol; 
	
	public Symbol(char c){
		switch(c) { 
			case 'a': 
				this.symbol = SymbolEnum.A; 
				break; 
			case 'b': 
				this.symbol = SymbolEnum.B; 
				break; 
			case 'e': 
				this.symbol = SymbolEnum.EPSILON; 
				break; 
			default: 
				this.symbol = SymbolEnum.INVALID; 
				break; 
		}
	}
	
	public SymbolEnum getType() {
		return this.symbol; 
	}
}
