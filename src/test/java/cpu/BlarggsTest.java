package cpu;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import core.MainModule;
import gpu.Display;
import gpu.MockDisplay;
import mmu.MMU;

public class BlarggsTest {

    protected Injector getNewInjector() {
        return Guice.createInjector(Modules.override(new MainModule()).with(new BlarggsTestModule()));
    }

    class BlarggsTestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(Display.class).to(MockDisplay.class);
            bind(MMU.class).to(BlarggsTestMMU.class);
        }
    }
}
