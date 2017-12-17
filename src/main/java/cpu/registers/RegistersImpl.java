package cpu.registers;

/**
 * Concrete class to implement Registers
 * */
public class RegistersImpl implements Registers {

    // int is sufficient to represent a register
    private int a, b, c, d, e, f, h, l, pc, sp;

    @Override
    public int read(Register register) {
        return 0;
    }

    @Override
    public void write(Register register, int data) {

    }
}
