package core;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import cpu.CPU;
import cpu.CPUImpl;
import cpu.alu.ALU;
import cpu.alu.ALUImpl;
import cpu.clock.Clock;
import cpu.instructions.InstructionExecutor;
import cpu.instructions.InstructionExecutorImpl;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import gpu.Display;
import gpu.DisplayImpl;
import gpu.GPU;
import gpu.GPUImpl;
import mmu.MMU;
import mmu.MMUImpl;
import mmu.cartridge.Cartridge;
import mmu.cartridge.CartridgeImpl;
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
        /* CPU */
        bind(Registers.class).to(RegistersImpl.class);
        bind(CPU.class).to(CPUImpl.class);
        bind(InstructionExecutor.class).to(InstructionExecutorImpl.class);
        bind(ALU.class).to(ALUImpl.class);

        /* GPU */
        bind(GPU.class).to(GPUImpl.class);
        bind(Display.class).to(DisplayImpl.class);

        /* MMU */
        bind(ROM.class).to(ROMImpl.class);
    }

    /**
     * Sets all memory spaces for MMU,
     * sets ROM
     * */
    @Provides @Inject @Singleton
    MMU provideMMU(GPU gpu, ROM rom, Clock clock) {
        List<MemorySpace> memorySpaces = new ArrayList<>();
        memorySpaces.add(rom);
        memorySpaces.add(new RAM());
        memorySpaces.add(gpu);
        memorySpaces.add(new IOMemory());
        memorySpaces.add(new ContinuousMemorySpace(ZERO_PAGE_START_ADDRESS, ZERO_PAGE_END_ADDRESS));

        MMU mmu = new MMUImpl(memorySpaces, clock);
        mmu.setROM(rom);
        return mmu;
    }
}
