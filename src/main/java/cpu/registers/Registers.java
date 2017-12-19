package cpu.registers;

/**
 * Represents the register manager for GameBoy registers
 * */
public interface Registers {

    enum Flag {ZERO, SUBTRACTION, HALF_CARRY, CARRY};

    /*
    * Represents the different registers in a GameBoy,
    * 2 Single registers can be joined together to form a double
    * register. Eg. A and F join to become AF
    * */
    enum Register {A, B, C, D, E, F, H, L, AF, BC, DE, HL, SP};

    /**
     * @return the value read from register
     * */
    int read(Register register);

    /**
     * Writes data to register
     * */
    void write(Register register, int data);

    /**
     * Sets/resets one of the four flags
     * */
    void setFlag(Flag flag, boolean val);

    /**
     * Checks if flag is set or not
     * */
    boolean getFlag(Flag flag);
}
