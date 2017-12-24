package cpu.registers;

/**
 * Represents the register manager for GameBoy registers
 * */
public interface Registers {

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
     * Returns the value of flag
     * */
    boolean getFlag(Flag flag);
}
