import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import cpu.CPU;
import cpu.CPUImpl;
import cpu.InstructionExecutor;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import gpu.Display;
import gpu.DisplayImpl;
import gpu.GPU;
import gpu.GPUImpl;
import mmu.MMU;
import mmu.MMUImpl;
import mmu.memoryspaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the module that has all the real implementations
 * */
public class MainModule extends AbstractModule {
    private static final int ZERO_PAGE_START_ADDRESS = 0xFF80;
    private static final int ZERO_PAGE_END_ADDRESS = 0xFFFF;

    @Override
    protected void configure() {
        bind(Registers.class).to(RegistersImpl.class);
        bind(CPU.class).to(CPUImpl.class);
        bind(GPU.class).to(GPUImpl.class);
        bind(Display.class).to(DisplayImpl.class);
        bind(ROM.class).to(ROMImpl.class);
    }

    /**
     * Sets all memory spaces for MMU,
     * sets ROM
     * */
    @Provides @Inject
    MMU provideMMU(GPU gpu, ROM rom) {
        List<MemorySpace> memorySpaces = new ArrayList<>();
        memorySpaces.add(rom);
        memorySpaces.add(new RAM());
        memorySpaces.add(gpu);
        memorySpaces.add(new IOMemory());
        memorySpaces.add(new ContinuousMemorySpace(ZERO_PAGE_START_ADDRESS, ZERO_PAGE_END_ADDRESS));

        MMU mmu = new MMUImpl(memorySpaces);
        mmu.setROM(rom);
        return mmu;
    }
}
