package cpu.interrupts;

public interface InterruptManager {

    void requestInterrupt(InterruptType interruptType);

    void setInterruptsEnabled(boolean interruptsEnabled);

    InterruptType getCurrentInterrupt();
}
