package cpu.registers;

import cpu.MicroOp;

/**
 * Represents micros ops for register reads and writes
 * */
public enum RegisterMicroOp implements MicroOp {
    /*
    * All the reads and writes possible on registers
    * */
    READ_A, READ_B, READ_C, READ_D, READ_E, READ_F, READ_H, READ_L,
    READ_AF, READ_BC, READ_DE, READ_HL, READ_SP, READ_PC,
    WRTIE_A, WRITE_B, WRITE_C, WRITE_D, WRITE_E, WRITE_F, WRITE_H, WRTIE_L,
    WRITE_AF, WRITE_BC, WRITE_DE, WRITE_HL, WRITE_SP, WRITE_PC;

    private static final int NUM_CYCLES = 0; // takes 0 cc to read or write to a register

    @Override
    public int getNumCycles() {
        return NUM_CYCLES;
    }
}
