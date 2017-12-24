package cpu;

import cpu.clock.Clock;
import cpu.clock.ClockImpl;
import cpu.instructions.InstructionExecutor;
import cpu.instructions.InstructionExecutorImpl;
import cpu.instructions.InstructionTimerImpl;
import cpu.registers.Register;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import mmu.MMU;
import mmu.MockMMU;
import org.junit.Test;

/**
 * Tests instruction executions
 *
 * Unfortunately because of time constraints these tests are not comprehensive,
 * they test what the instructions are supposed to do,
 * but they don't test that the instructions don't do anything more than that
 * */
public class TestInstructionExecution {

    private InstructionExecutor instructionExecutor;
    private Registers registers;
    private Clock clock;
    private MMU mmu;

    public TestInstructionExecution() {
        registers = new RegistersImpl();
        clock = new ClockImpl();
        mmu = new MockMMU();
        instructionExecutor = new InstructionExecutorImpl(mmu, registers, clock, new InstructionTimerImpl());
    }

    @Test
    public void testInstructions() {
        mmu.write(0, 0x00);
        executeInstructionAndTestPCAndClock(1, 4);
    }

    private void executeInstructionAndTestPCAndClock(int pcIncrement, int cycleIncrement) {
        int oldPC = registers.read(Register.PC);
        int oldCycles = clock.getTotalCycles();

        instructionExecutor.executeInstruction();

        assert (registers.read(Register.PC) - oldPC == pcIncrement);
        assert (clock.getTotalCycles() - oldCycles == cycleIncrement);
    }
}
