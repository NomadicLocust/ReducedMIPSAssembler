Reduced MIPS Assembler
======================

This is a Java-based assembler with support for a reduced MIPS instruction set.

To assemble a file, run:
`java ReducedMIPS filename [-d]`

(-d sets debug mode)

---

The following instructions are supported:
**R-Type**
* add $rd, $rs, $rt
* sub $rd, $rs, $rt
* and $rd, $rs, $rt
* or $rd, $rs, $rt
* nor $rd, $rs, $rt
* slt $rd, $rs, $rt
* sll $rd, $rt, shamt
* srl $rd, $rt, shamt
* jr $rs

**I-Type**
* addi $rt, $rs, imm
* andi $rt, $rs, imm
* ori $rt, $rs, imm
* beq $rs, $rt, offset
* bne $rs, $rt, offset
* lw $rt, offset($rs)
* sw $rt, offset($rs)

-
**J-Type**
* j target
* jal target
