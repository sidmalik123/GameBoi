import com.google.inject.Guice;
import com.google.inject.Injector;
import core.MainModule;
import cpu.CPU;
import cpu.clock.Clock;
import gpu.GPU;
import mmu.MMU;
import mmu.cartridge.CartridgeImpl;
import timers.Timer;

import java.io.IOException;

public class GameBoy {

    public static void main(String[] args) throws IOException {
        Injector mainInjector = Guice.createInjector(new MainModule());
        MMU mmu = mainInjector.getInstance(MMU.class);
        mmu.load(new CartridgeImpl("src/test/java/cpu/cpu_instrs/individual/02-interrupts.gb"));
        CPU cpu = mainInjector.getInstance(CPU.class);
        Clock clock = mainInjector.getInstance(Clock.class);
        clock.attach(mainInjector.getInstance(GPU.class));
        clock.attach(mainInjector.getInstance(Timer.class));
        cpu.run();
    }
}
