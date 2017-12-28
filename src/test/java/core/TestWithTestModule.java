package core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

public class TestWithTestModule {

    protected Injector testInjector;

    public TestWithTestModule() {
        testInjector = Guice.createInjector(Modules.override(new MainModule()).with(new TestModule()));
    }
}
