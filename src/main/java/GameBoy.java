import com.google.inject.Guice;
import com.google.inject.Inject;
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

    private MMU mmu;
    private CPU cpu;

    @Inject
    public GameBoy(CPU cpu, MMU mmu, Clock clock, GPU gpu, Timer timer) {
        this.cpu = cpu;
        this.mmu = mmu;
        clock.attach(gpu);
        clock.attach(timer);
    }

    private void run(String gameLocation) {
        mmu.load(new CartridgeImpl(gameLocation));
        cpu.run();
    }

    public static void main(String[] args) throws IOException {
        Injector mainInjector = Guice.createInjector(new MainModule());
        GameBoy gameBoy = mainInjector.getInstance(GameBoy.class);
        gameBoy.run("roms/Dr. Mario.gb");
    }
}
