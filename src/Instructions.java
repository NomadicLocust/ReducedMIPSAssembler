import java.util.HashMap;


public class Instructions {
	public static HashMap<String, String> instructionCodes = new HashMap<String, String>();
	public static HashMap<String, instructionParser> instructions = new HashMap<String, instructionParser>();
	
	private Instructions() {
	}

	public static void initInstructionCodes() {
		// R-Type Instructions
		instructionCodes.put("add", "100000");
		instructionCodes.put("sub", "100010");
		instructionCodes.put("and", "100100");
		instructionCodes.put("or",  "100101");
		instructionCodes.put("nor", "100111");
		instructionCodes.put("slt", "101010");
		instructionCodes.put("sll", "000000");
		instructionCodes.put("srl", "000010");
		instructionCodes.put("jr",  "001000");
		
		// I-Type Instructions
		instructionCodes.put("addi", "001000");
		instructionCodes.put("andi", "001110");
		instructionCodes.put("ori",  "001101");
		instructionCodes.put("beq",  "000100");
		instructionCodes.put("bne",  "000101");
		instructionCodes.put("lw",   "100011");
		instructionCodes.put("sw",   "101011");
		
		// J-Type Instructions
		instructionCodes.put("j",   "000010");
		instructionCodes.put("jal", "000011");
	}
	
	public static void initInstructions() {
		// R-Type Instructions
		instructions.put("add",	instructionR_std);
		instructions.put("sub", instructionR_std);
		instructions.put("and", instructionR_std);
		instructions.put("or",  instructionR_std);
		instructions.put("nor", instructionR_std);
		instructions.put("slt", instructionR_std);
		instructions.put("sll", instructionR_shift);
		instructions.put("srl", instructionR_shift);
		instructions.put("jr",  instructionR_jr);
		
		// I-Type Instructions
		instructions.put("addi", instructionI_std);
		instructions.put("andi", instructionI_std);
		instructions.put("ori",  instructionI_std);
		instructions.put("beq",  instructionI_branch);
		instructions.put("bne",  instructionI_branch);
		instructions.put("lw",   instructionI_word);
		instructions.put("sw",   instructionI_word);
		
		// J-Type Instructions
		instructions.put("j",	instructionJ);
		instructions.put("jal", instructionJ);
	}
	
	public interface instructionParser {
		void parse(String [] parts);		
	}
	
	// Instructions: add, sub, and, or, nor, slt
	public static instructionParser instructionR_std = new instructionParser() {
		public void parse(String [] parts) {
			String opcode = "000000"; //instrCode.substring(2, 8);
			String rs = Assembler.getRegister(parts[2]);
			String rt = Assembler.getRegister(parts[3]);
			String rd = Assembler.getRegister(parts[1]);
			String shamt = "00000";
			String funct = instructionCodes.get(parts[0]);
			
			System.out.println(opcode + rs + rt + rd + shamt + funct);
		}
	};
	
	// Instructions: sll, srl
	public static instructionParser instructionR_shift = new instructionParser() {
		public void parse(String [] parts) {
			String opcode = "000000";
			String rs = "00000";
			String rt = Assembler.getRegister(parts[2]);
			String rd = Assembler.getRegister(parts[1]);
			String shamt = Assembler.parseUnsigned5BitBin(Integer.parseInt(parts[3]));
			String funct = instructionCodes.get(parts[0]);
			
			System.out.println(opcode + rs + rt + rd + shamt + funct);
		}
	};
	
	// Instructions: jr
	public static instructionParser instructionR_jr = new instructionParser() {
		public void parse(String [] parts) {
			String opcode = "000000";
			String rs = Assembler.getRegister(parts[1]);
			String rt = "00000";
			String rd = "00000";
			String shamt = "00000";
			String funct = instructionCodes.get(parts[0]);
			
			System.out.println(opcode + rs + rt + rd + shamt + funct);
		}
	};
	
	// Instructions: addi, andi, ori
	public static instructionParser instructionI_std = new instructionParser() {
		public void parse(String [] parts) {
			String opcode = instructionCodes.get(parts[0]);
			String rs = Assembler.getRegister(parts[2]);
			String rt = Assembler.getRegister(parts[1]);
			String immediate = Assembler.parseSigned16BitBin(Integer.parseInt(parts[3]));
			
			System.out.println(opcode + rs + rt + immediate);
		}
	};
	
	// Instructions: beq, bne
	public static instructionParser instructionI_branch = new instructionParser() {
		public void parse(String [] parts) {
			String opcode = instructionCodes.get(parts[0]);
			String rs = Assembler.getRegister(parts[1]);
			String rt = Assembler.getRegister(parts[2]);
			String immediate = Assembler.parseSigned16BitBin( Assembler.labels.get(parts[3]) - Assembler.lineNumber - 1 );
			
			System.out.println(opcode + rs + rt + immediate);
		}
	};
	
	// Instructions: lw, sw
	public static instructionParser instructionI_word = new instructionParser() {
		public void parse(String [] parts) {
			String opcode = instructionCodes.get(parts[0]);
			String rs = Assembler.getRegister(parts[3]);
			String rt = Assembler.getRegister(parts[1]);
			String immediate = Assembler.parseSigned16BitBin(Integer.parseInt(parts[2]));
			
			System.out.println(opcode + rs + rt + immediate);
		}
	};
	
	// Instructions: j, jal
	public static instructionParser instructionJ = new instructionParser() {
		public void parse(String [] parts) {
			String opcode = instructionCodes.get(parts[0]);
			// Compute the jump address and crop to 26 bits
			int truncAddress = 0x00400000 + 4*(Assembler.labels.get(parts[1]) - 1); 
			String address = Assembler.parseUnsigned32BitBin(truncAddress).substring(4, 30);
			
			System.out.println(opcode + address);
		}
	};
}
