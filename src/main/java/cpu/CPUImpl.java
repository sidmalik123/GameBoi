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
        while (true) { // Todo - if after completion this still does not have much logic move this loop into GameBoy.class
            instructionExecutor.executeInstruction();
        }
    }
}
