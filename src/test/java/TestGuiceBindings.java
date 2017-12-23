import com.google.inject.Guice;
import com.google.inject.Injector;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests Guice bindings and scopes
 * */
public class TestGuiceBindings {

    private Injector injector = Guice.createInjector(new MainModule());;

    @Test
    public void testRegistersBinding() {
        Registers registers = injector.getInstance(Registers.class);
        Registers registers1 = injector.getInstance(Registers.class);

        assert (registers instanceof RegistersImpl); // check binding
        assert (registers == registers1); // check singleton scope
    }
}
