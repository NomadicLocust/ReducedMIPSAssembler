// Partial MIPS Assembler
//
// Author: Sam H.
// Last Edited: 3/10/14

import java.util.Scanner;


public class ReducedMIPS {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String filename;
		
		// Initialize HashMaps
		Assembler.initInstructionCodes();
		Assembler.initInstructions();
		Assembler.initRegisterCodes();
		
		// Get filename from command line if provided, else use stdin
		if (args.length > 0) {
            filename = args[0];
            for (int i=1; i<args.length; i++) {
                // Set debug mode if command line option is set
                if (args[i].equals("-d")) Assembler.debugMode = true;
                // else if (args[i].equals("-p")) printToConsole = true;
            }
		}
		else {
			System.out.print("Enter name of file to assemble: ");
			filename = sc.next();
		}
		
		Assembler.assembleFile(filename);
		
		sc.close();

	}
	
}
