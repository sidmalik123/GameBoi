package cpu.instructions;

/**
 * Executes an instruction
 * */
public interface InstructionExecutor {

    /**
     * Executes instruction
     *
     * @return the number of clock cycles taken to execute instruction
     * */
    int executeInstruction();
}
