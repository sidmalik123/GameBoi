package cpu.registers;

/**
 * Represents the register manager for GameBoy registers
 * */
public interface Registers {

    /*
    * Represents the different registers in a GameBoy,
    * Single registers can be joined together to form double
    * registers. Eg. A and F join to become AF
    * */
    enum Register {A, B, C, D, E, F, H, L, AF, BC, DE, HL};

    /**
     * @return the value read from register
     * */
    int read(Register register);

    /**
     * Writes data to register
     * */
    void write(Register register, int data);
}
