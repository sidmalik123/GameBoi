package cpu;

public abstract class AbstractCPU implements CPU {

    protected abstract int readInstruction(int address);

    protected abstract void executeInstruction(int instruction);
}
