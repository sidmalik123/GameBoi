package cpu.interrupts;

public interface InterruptSubject {

    void requestInterrupt(InterruptType interruptType);

    boolean interruptsEnabled();
}
