import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import core.MainModule;
import cpu.CPU;
import cpu.clock.Clock;
import gpu.GPU;
import mmu.MMU;
import mmu.cartridge.CartridgeImpl;
import timers.Timers;

import java.io.IOException;

public class GameBoy {

    private MMU mmu;
    private CPU cpu;

    @Inject
    public GameBoy(CPU cpu, MMU mmu, Clock clock, GPU gpu, Timers timers) {
        this.cpu = cpu;
        this.mmu = mmu;
        clock.attach(gpu);
        clock.attach(timers);
    }

    private void run(String gameLocation) {
        mmu.load(new CartridgeImpl(gameLocation));
        while (true) {
            cpu.executeInstruction();
        }
    }

    public static void main(String[] args) throws IOException {
        Injector mainInjector = Guice.createInjector(new MainModule());
        GameBoy gameBoy = mainInjector.getInstance(GameBoy.class);
        gameBoy.run("roms/instr_timing.gb");
    }
}
