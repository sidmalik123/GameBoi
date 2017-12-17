package cpu.registers;

import core.BitUtils;

/**
 * Concrete class to implement Registers
 * */
public class RegistersImpl implements Registers {

    // int is sufficient to represent a register
    private int a, b, c, d, e, f, h, l, sp;

    @Override
    public int read(Register register) {
        switch (register) {
            case A:
                return a;
            case B:
                return b;
            case C:
                return c;
            case D:
                return d;
            case E:
                return e;
            case F:
                return f;
            case H:
                return h;
            case L:
                return l;
            case AF:
                return BitUtils.joinBytes(a, f);
            case BC:
                return BitUtils.joinBytes(b, c);
            case DE:
                return BitUtils.joinBytes(d, e);
            case HL:
                return BitUtils.joinBytes(h, l);
            case SP:
                return sp;
            default:
                throw new IllegalArgumentException("Read not implemented for register " + register);
        }
    }

    @Override
    public void write(Register register, int data) {
        switch (register) {
            case A:
                a = data & 0xFF;
            case B:
                b = data & 0xFF;
            case C:
                c = data & 0xFF;
            case D:
                d = data & 0xFF;
            case E:
                e = data & 0xFF;
            case F:
                f = data & 0xFF;
            case H:
                h = data & 0xFF;
            case L:
                l = data & 0xFF;
            case AF:
                a = BitUtils.getHighByte(data);
                f = BitUtils.getLowByte(data);
            case BC:
                b = BitUtils.getHighByte(data);
                c = BitUtils.getLowByte(data);
            case DE:
                d = BitUtils.getHighByte(data);
                e = BitUtils.getLowByte(data);
            case HL:
                h = BitUtils.getHighByte(data);
                l = BitUtils.getLowByte(data);
            case SP:
                sp = data & 0xFFFF;
        }
    }
}
