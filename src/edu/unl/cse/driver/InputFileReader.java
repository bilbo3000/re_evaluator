package edu.unl.cse.driver;

/**
 * Process input file. 
 * 
 * @author Dongpu
 *
 */

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader; 

public class InputFileReader {
	/* 
	 * Read lines from the file redirected from command line. 
	 */ 
	public static String[] getLines() throws IOException {
		ArrayList<String> lines = new ArrayList<String>();
		String line; 
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in)); 
        while ((line = r.readLine()) != null) {
            lines.add(line); 
        }
		
		String[] result = lines.toArray(new String[0]);
		
		return result;
	}
}
