package mmu;

import gpu.GPUImpl;
import mmu.memoryspaces.*;

import java.util.ArrayList;
import java.util.List;

public class MMUFactoryImpl implements MMUFactory {

    private static final int ZERO_PAGE_START_ADDRESS = 0xFF80;
    private static final int ZERO_PAGE_END_ADDRESS = 0xFFFF;

    @Override
    public MMU create() {
        List<MemorySpace> memorySpaces = new ArrayList<>();
        memorySpaces.add(new ROM());
        memorySpaces.add(new RAM());
        memorySpaces.add(new GPUImpl());
        memorySpaces.add(new IOMemory());
        memorySpaces.add(new ContinuousMemorySpace(ZERO_PAGE_START_ADDRESS, ZERO_PAGE_END_ADDRESS));
        MMU mmu = new MMUImpl(memorySpaces);

        return mmu;
    }
}
