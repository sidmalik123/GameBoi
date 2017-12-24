package cpu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cpu.clock.ClockObserver;
import cpu.instructions.InstructionExecutor;

@Singleton
public class CPUImpl implements CPU {

    private InstructionExecutor instructionExecutor;

    @Inject
    public CPUImpl(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    @Override
    public void run() {
        while (true) {
            int numCycles = instructionExecutor.executeInstruction();
        }
    }
}
