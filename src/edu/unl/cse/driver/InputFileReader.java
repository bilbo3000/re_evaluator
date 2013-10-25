package edu.unl.cse.driver;

/**
 * Process input files. 
 * 
 * @author Dongpu
 *
 */

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class InputFileReader {
	private ArrayList<String> lines = new ArrayList<String>();

	public void readInputFile(String filePath) throws IOException {
		File inFile = new File(filePath);
		FileReader inFileReader = new FileReader(inFile);
		BufferedReader bufferedReader = new BufferedReader(inFileReader);

		String line = bufferedReader.readLine();

		while (line != null) {
			lines.add(line);
			line = bufferedReader.readLine();
		}

		bufferedReader.close();
	}

	public String[] getLines() {
		String[] result = this.lines.toArray(new String[0]);
		return result;
	}
}
