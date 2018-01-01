package core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

public class TestWithMockDisplay {

    protected Injector testInjector;

    public TestWithMockDisplay() {
        testInjector = Guice.createInjector(Modules.override(new MainModule()).with(new TestModule()));
    }
}
