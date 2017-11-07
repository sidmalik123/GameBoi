package cpu;

import core.Address;
import core.Instruction;

public abstract class AbstractCPU<T extends Instruction<?>, Y extends Address<?>> implements CPU {

    protected abstract T readInstruction(Y address);

    protected abstract void executeInstruction(T instruction);
}
