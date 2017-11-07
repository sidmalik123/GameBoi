package mmu;

import core.Address;
import core.Instruction;

public interface MMU<T, Y extends Address<?>> {

    T readData(Y address);

    void wrtieData(Y address, T data);

}
