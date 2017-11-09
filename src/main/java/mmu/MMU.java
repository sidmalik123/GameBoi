package mmu;

public interface MMU<T, Y> {

    T readData(Y address);

    void writeData(Y address, T data);

}
