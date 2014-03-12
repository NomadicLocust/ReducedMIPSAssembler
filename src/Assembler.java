import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;


public class Assembler {
	
	public static File file;
	public static int lineNumber = 1;
	
	public static boolean debugMode = false;
	
	public static HashMap<String, String> registers = new HashMap<String, String>();
	public static HashMap<String, Integer> labels = new HashMap<String, Integer>();
    
	private Assembler() {
	}
	
	public static void initRegisterCodes() {
		// Constant 0
		registers.put("$zero", "00000");
		// Assembler temporary
		registers.put("$at",   "00001");
		
		// Function results & expression evaluation
		registers.put("$v0",   "00010");
		registers.put("$v1",   "00011");
		
		// Arguments
		registers.put("$a0",   "00100");
		registers.put("$a1",   "00101");
		registers.put("$a2",   "00110");
		registers.put("$a3",   "00111");
		
		// Temporaries
		registers.put("$t0",   "01000");
		registers.put("$t1",   "01001");
		registers.put("$t2",   "01010");
		registers.put("$t3",   "01011");
		registers.put("$t4",   "01100");
		registers.put("$t5",   "01101");
		registers.put("$t6",   "01110");
		registers.put("$t7",   "01111");
		
		// Saved temporaries
		registers.put("$s0",   "10000");
		registers.put("$s1",   "10001");
		registers.put("$s2",   "10010");
		registers.put("$s3",   "10011");
		registers.put("$s4",   "10100");
		registers.put("$s5",   "10101");
		registers.put("$s6",   "10110");
		registers.put("$s7",   "10111");
		
		// Temporaries
		registers.put("$t8",   "11000");
		registers.put("$t9",   "11001");
		
		// Reserved for OS Kernel
		registers.put("$k0",   "11010");
		registers.put("$k1",   "11011");
		
		// Global pointer
		registers.put("$gp",   "11100");
		// Stack pointer
		registers.put("$sp",   "11101");
		// Frame pointer
		registers.put("$fp",   "11110");
		// Return address
		registers.put("$ra",   "11111");
	}	
	
	// Returns unsigned 5-bit binary representation of decimal value
	public static String parseUnsigned5BitBin(int dec) {
		//int decValue = Integer.parseInt(dec); this was used when argument was a string
		String bin = Integer.toBinaryString(dec);
		
		int l = bin.length();
		if (bin.length() < 5) {
			for (int i=0; i<(5-l); i++)
				bin = "0" + bin;
		}
		
		return bin;
	}
	
	// Returns signed 16-bit binary representation of decimal value
	public static String parseSigned16BitBin(int dec) {
		//int decValue = Integer.parseInt(dec);
		String bin = Integer.toBinaryString(dec);
		
		int l = bin.length();
		if (bin.length() < 16 && dec >= 0) {
			for (int i=0; i<(16-l); i++)
				bin = "0" + bin;
		}
		else if (dec < 0) {
			bin = bin.substring(l - 16);
		}
		
		return bin;
	}
	
	// Returns unsigned 16-bit binary representation of decimal value
	// public static String parseUnsigned16BitBin(int dec) {
	// 	//int decValue = Integer.parseInt(dec); this was used when argument was a string
	// 	String bin = Integer.toBinaryString(dec);
		
	// 	int l = bin.length();
	// 	if (bin.length() < 16) {
	// 		for (int i=0; i<(16-l); i++)
	// 			bin = "0" + bin;
	// 	}
		
	// 	return bin;
	// }
    
	
	// Returns unsigned 32-bit binary representation of decimal value
	// (for use in J-Format instruction)
	public static String parseUnsigned32BitBin(int dec) {
		String bin = Integer.toBinaryString(dec);
		
		int l = bin.length();
		if (bin.length() < 32) {
			for (int i=0; i<(32-l); i++)
				bin = "0" + bin;
		}
		
		return bin;
	}
	
	// Returns the register address as a String
	public static String getRegister(String reg) {
		// Numeral address reference, e.g. $8
		if (reg.matches("[$]\\d+"))
			return parseUnsigned5BitBin( Integer.parseInt(reg.substring(1)) );
		// Standard reference, e.g. $t0
		return registers.get(reg);
	}
	
	// Run assembly process on file with given filename
	public static void assembleFile(String filename) {
		file = new File(filename);
		
		getLabels();
		assemble();
	}
	
	// Scan file for labels and add their reference to the labels HashMap
	public static void getLabels() {
		try {
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				line = line.trim(); // Trim leading & trailing white space
				
				// If line contains a label
				if (line.matches(".+:.*")) {
					String labelName = line.substring(0, line.indexOf(':'));
					labels.put(labelName, lineNumber);
					// Debugging mode displays label names & their associated line numbers
					if (debugMode) System.out.println(labelName + ":  " + lineNumber);
				}
				
				// Remove labels from the line
				// This is done to check if line is empty & whether or not to increment line number)
				line = line.replaceAll("^.+:([\\s]+)?", "");
				
				if (!line.isEmpty())	
					lineNumber++;
			}
			
			scanner.close();
			lineNumber = 1;
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}		
	}
	
	// Perform actual assembly of the instructions into binary
	public static void assemble() {
		try {
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
						
				line = line.trim(); // Trim leading & trailing white space
				line = line.replaceAll("^.+:([\\s]+)?", ""); // Remove labels from the line
                line = line.replaceAll("[#].+", ""); // Remove comments
				line = line.replace("(", ","); // This line and the following one format to allow for sw & lw instructions
				line = line.replace(")", "");
				
				// Do not try to parse line if it is blank or contains only white space/tabs
				if (line.isEmpty())
					continue;
				
				// Split into each word by commas & white space
				String [] parts = line.split("[,\\s]+");
				
				// This section is for debugging purposes
				if (debugMode) {
					System.out.println();
					for (int i=0; i<parts.length; i++) System.out.print("[" + parts[i] + "] ");
					System.out.println();
					System.out.print(lineNumber + ": ");
				}
				
				// Parse and write instruction
				Instructions.instructions.get(parts[0]).parse(parts);
				
				lineNumber++;
			}
			
			scanner.close();
		}
		catch (FileNotFoundException e) {
            		// Do not print anything since parseLabels() already took care of that.
			// System.out.println("File not found.");
		}
	}
}
