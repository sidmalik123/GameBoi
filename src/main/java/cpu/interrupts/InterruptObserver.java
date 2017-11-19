package cpu.interrupts;

public interface InterruptObserver {

    void notifyInterupt(InterruptType interruptType);
}
