package core;

import com.google.inject.AbstractModule;
import cpu.CPU;
import cpu.CPUImpl;
import cpu.MemoryAccessor;
import cpu.MemoryAccessorImpl;
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
import timers.Timer;
import timers.TimerImpl;

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
        bind(MemoryAccessor.class).to(MemoryAccessorImpl.class);

        /* Interrupts */
        bind(InterruptManager.class).to(InterruptManagerImpl.class);

        /* GPU */
        bind(GPU.class).to(GPUImpl.class);
        bind(Display.class).to(DisplayImpl.class);

        /* MMU */
        bind(MMU.class).to(MMUImpl.class);

        /* Timers */
        bind(Timer.class).to(TimerImpl.class);
    }
}
