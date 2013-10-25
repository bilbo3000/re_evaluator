package edu.unl.cse.driver;

import java.io.File;
import java.io.IOException;

import edu.unl.cse.evaluator.StringEvaluator;
import edu.unl.cse.nfa2dfa.NFA2DFAConverter;
import edu.unl.cse.postfix2nfa.Postfix2NFAConverter;
import edu.unl.cse.re2postfix.RE2PostfixConverter;
import edu.unl.cse.utils.State;

/**
 * The driver class initiate the program. 
 * @author Dongpu
 *
 */
public class REDriver {
	public static void main(String[] args) {
		// Process input files
		File currDir = new File("input");
		File[] files = currDir.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				InputFileReader fileHandler = new InputFileReader();

				try {
					fileHandler.readInputFile(file.getAbsolutePath());
				} catch (IOException e) {
					System.out.println("Fail to process input file: "
							+ file.getName());
					System.exit(1);
				}

				String[] lines = fileHandler.getLines();
				String re = lines[0]; // First line is regular expression

				// Convert RE from infix to postfix.
				RE2PostfixConverter postfixConverter = new RE2PostfixConverter();
				String postfixRE = postfixConverter.convert(re);

				// Convert from postfix to NFA.
				State nfa = new Postfix2NFAConverter().convert(postfixRE);

				// Convert from NFA to DFA.
				State dfa = new NFA2DFAConverter().convert(nfa);

				// Evaluate strings and print out result
				StringEvaluator evaluator = new StringEvaluator();

				for (int i = 1; i < lines.length; i++) {
					String line = lines[i];
					if (evaluator.evaluate(line, dfa)) {
						System.out.println("yes");
					} else {
						System.out.println("no");
					}
				}
			}
		}
	}
}
