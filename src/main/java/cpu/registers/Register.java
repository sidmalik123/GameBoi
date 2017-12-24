package cpu.registers;

/**
* Represents the different registers in a GameBoy,
* 2 Single registers can be joined together to form a double
* register. Eg. A and F join to become AF
* */
public enum Register {
    A, B, C, D, E, F, H, L, AF, BC, DE, HL, SP, PC;
}
