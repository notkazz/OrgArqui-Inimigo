.text
.globl main
main:
addiu $t2, $t1, 387
addu $t3, $t2, $t7
sw $t3, 4($t1)
lw $t4, 4($t1)
lui $t5, 943
beq $s0, $s1, L2

L1:
xor $t6, $t5, $t3
and $t7, $t5, $t3
andi $t8, $t5, 183
ori $t9, $t7, 290

L2:
bne $s0, $t3, L1