package cpu;

import cpu.instructions.InstructionTimer;
import cpu.instructions.InstructionTimerImpl;
import org.junit.Test;

public class TestInstructionTimings {

    private InstructionTimer instructionTimer;

    public TestInstructionTimings() {
        instructionTimer = new InstructionTimerImpl();
    }

    @Test
    public void testInstructionTimings() {
        assert (instructionTimer.getNumCycles(0x00) == 4);
    }
}
