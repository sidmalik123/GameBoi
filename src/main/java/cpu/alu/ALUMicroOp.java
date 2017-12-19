package cpu.alu;

import cpu.microops.MicroOp;

/**
 * Represents Microps possible in GameBoy's ALU
 * */
public enum ALUMicroOp implements MicroOp {

    JOIN_BYTES, CMP, XOR, SUB_CARRY_BYTES, DEC_WORD, INC_BYTE,
    TEST_BIT_0, TEST_BIT_1, TEST_BIT_2, TEST_BIT_3, TEST_BIT_4, TEST_BIT_5, TEST_BIT_6, TEST_BIT_7,
    ADD_BYTES;

    @Override
    public int getNumCycles() {
        switch (this) {
            case JOIN_BYTES:
            case CMP:
            case XOR:
            case SUB_CARRY_BYTES:
            case ADD_BYTES:
            case INC_BYTE:
                return 0;
            case DEC_WORD:
            case TEST_BIT_0:
            case TEST_BIT_1:
            case TEST_BIT_2:
            case TEST_BIT_3:
            case TEST_BIT_4:
            case TEST_BIT_5:
            case TEST_BIT_6:
            case TEST_BIT_7:
                return 4;
        }
        throw new IllegalArgumentException("Unknown ALUOp" + this);
    }
}
