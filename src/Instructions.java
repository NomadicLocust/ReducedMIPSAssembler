import java.util.HashMap;


public class Instructions {
	public static void initInstructionCodes() {
		// R-Type Instructions
		instructionCodes.put("add",	"100000");
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
		instructions.put("j",	instructionR_std);
		instructions.put("jal", instructionR_std);
	}
	
	public interface instructionParser {
		void parse(String [] parts);		
	}
}
