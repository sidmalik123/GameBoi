package cpu;

public abstract class AbstractCPU implements CPU {

    protected abstract int readInstruction();

    protected abstract void executeInstruction(int instruction);
}
