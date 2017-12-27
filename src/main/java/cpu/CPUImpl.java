package cpu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cpu.clock.ClockObserver;
import cpu.instructions.InstructionExecutor;

@Singleton
public class CPUImpl implements CPU {

    private InstructionExecutor instructionExecutor;
    private boolean isStopped;
    private boolean isHalted;

    @Inject
    public CPUImpl(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    @Override
    public void run() {
        while (!isStopped && !isHalted) {
            instructionExecutor.executeInstruction();
        }
    }

    @Override
    public void stop() {
        this.isStopped = true;
    }

    @Override
    public void halt() {
        this.isHalted = true;
    }
}
