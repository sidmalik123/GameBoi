package mmu;

public interface MMU<T, Y> {

    T readData(Y address);

    void wrtieData(Y address, T data);

}
