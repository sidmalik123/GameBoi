package cpu;

import cpu.clock.ClockObserver;
import cpu.clock.ClockSubject;

public class CPUImpl extends ClockSubject implements CPU {

    private InstructionExecutor instructionExecutor;

    public CPUImpl(InstructionExecutor instructionExecutor) {
        this.instructionExecutor = instructionExecutor;
    }

    @Override
    public void run() {
        while (true) {
            instructionExecutor.executeInstruction();
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
