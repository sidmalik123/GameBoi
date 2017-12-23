package cpu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cpu.clock.ClockObserver;
import cpu.clock.ClockSubject;

@Singleton
public class CPUImpl extends ClockSubject implements CPU {

    private InstructionExecutor instructionExecutor;

    @Inject
    public CPUImpl(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    @Override
    public void run() {
        while (true) {
            int numCycles = instructionExecutor.executeInstruction();
            notifyClockIncrement(numCycles);
        }
    }

    /**
     * Notifies all of its observers to handle clock increment
     * */
    @Override
    protected void notifyClockIncrement(int increment) {
        for (ClockObserver observer : this.observers) {
            observer.handleClockIncrement(increment);
        }
    }
}
