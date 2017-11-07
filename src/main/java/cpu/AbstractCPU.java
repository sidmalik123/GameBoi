package cpu;

public abstract class AbstractCPU<T, Y> implements CPU {

    protected abstract T readInstruction(Y address);

    protected abstract void executeInstruction(T instruction);
}
