import com.google.inject.AbstractModule;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;

/**
 * This is the module that has all the real implementations
 * */
public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Registers.class).to(RegistersImpl.class);
    }
}
