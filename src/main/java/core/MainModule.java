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
import cpu.clock.ClockImpl;
import cpu.instructions.InstructionExecutor;
import cpu.instructions.InstructionExecutorImpl;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import gpu.Display;
import gpu.DisplayImpl;
import gpu.GPU;
import gpu.GPUImpl;
import interrupts.InterruptManager;
import interrupts.InterruptManagerImpl;
import mmu.MMU;
import mmu.MMUImpl;
import mmu.memoryspaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the module that has all the real implementations
 * */
public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        /* CPU */
        bind(Registers.class).to(RegistersImpl.class);
        bind(CPU.class).to(CPUImpl.class);
        bind(InstructionExecutor.class).to(InstructionExecutorImpl.class);
        bind(ALU.class).to(ALUImpl.class);
        bind(Clock.class).to(ClockImpl.class);

        /* Interrupts */
        bind(InterruptManager.class).to(InterruptManagerImpl.class);

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
    MMU provideMMU(GPU gpu, ROM rom, Clock clock, InterruptManager interruptManager) {
        List<MemorySpace> memorySpaces = new ArrayList<>();
        memorySpaces.add(rom);
        memorySpaces.add(new RAM());
        memorySpaces.add(gpu);
        memorySpaces.add(new RestMemory());
        memorySpaces.add(interruptManager);
        memorySpaces.add(new RestrictedMemory());

        MMU mmu = new MMUImpl(memorySpaces, clock);
        mmu.setROM(rom);
        return mmu;
    }
}
