package cpu.instructions;

import com.google.inject.Inject;
import cpu.clock.Clock;
import cpu.registers.Register;
import cpu.registers.Registers;
import mmu.MMU;

/**
 * Concrete class for InstructionExecutor
 * */
public class InstructionExecutorImpl implements InstructionExecutor {

    private MMU mmu;
    private Registers registers;
    private Clock clock;
    private InstructionTimer instructionTimer;

    @Inject
    public InstructionExecutorImpl(MMU mmu, Registers registers, Clock clock, InstructionTimer instructionTimer) {
        this.mmu = mmu;
        this.registers = registers;
        this.clock = clock;
        this.instructionTimer = instructionTimer;
    }

    @Override
    public void executeInstruction() {
        int instruction = getNextInstruction();

        switch (instruction) {
            case 0x00: break;
        }

        clock.addCycles(instructionTimer.getNumCycles(instruction));
    }

    /**
     * Returns the next instruction
     * */
    private int getNextInstruction() {
        int pc = registers.read(Register.PC);
        int instruction = mmu.read(pc);
        registers.write(Register.PC, ++pc);

        return instruction;
    }
}
