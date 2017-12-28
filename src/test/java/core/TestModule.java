package core;

import com.google.inject.AbstractModule;
import gpu.Display;
import gpu.MockDisplay;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Display.class).to(MockDisplay.class);
    }
}
