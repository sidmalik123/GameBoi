package mmu;

public interface MemorySpace<T, Y> {

    T read(Y address);

    void write(Y address, T data);
}
