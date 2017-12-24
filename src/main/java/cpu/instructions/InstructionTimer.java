package cpu.instructions;

/**
 * Times all instructions
 * */
public interface InstructionTimer {

    /**
     * Returns the number of clock cycles it takes to execute instruction
     * */
    int getNumCycles(int instruction);
}
