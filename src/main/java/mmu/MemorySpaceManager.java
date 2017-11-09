package mmu;

public interface MemorySpaceManager<T extends MemorySpace, Y> {

    T getMemorySpace(Y address);
}
