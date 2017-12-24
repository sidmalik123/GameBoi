package cpu.registers;

import com.google.inject.Singleton;
import core.BitUtils;

/**
 * Concrete class to implement Registers
 * */
@Singleton
public class RegistersImpl implements Registers {

    // int is sufficient to represent a register
    private int a, b, c, d, e, f, h, l, sp, pc;

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
            case PC:
                return pc;
            default:
                throw new IllegalArgumentException("Read not implemented for register " + register);
        }
    }

    @Override
    public void write(Register register, int data) {
        switch (register) {
            case A:
                a = data & 0xFF; break;
            case B:
                b = data & 0xFF; break;
            case C:
                c = data & 0xFF; break;
            case D:
                d = data & 0xFF; break;
            case E:
                e = data & 0xFF; break;
            case F:
                f = data & 0xFF; break;
            case H:
                h = data & 0xFF; break;
            case L:
                l = data & 0xFF; break;
            case AF:
                a = BitUtils.getHighByte(data);
                f = BitUtils.getLowByte(data);
                break;
            case BC:
                b = BitUtils.getHighByte(data);
                c = BitUtils.getLowByte(data);
                break;
            case DE:
                d = BitUtils.getHighByte(data);
                e = BitUtils.getLowByte(data);
                break;
            case HL:
                h = BitUtils.getHighByte(data);
                l = BitUtils.getLowByte(data);
                break;
            case SP:
                sp = data & 0xFFFF; break;
            case PC:
                pc = data & 0xFFFF; break;
        }
    }

    @Override
    public void setFlag(Flag flag, boolean val) {
        final int bitNum = flag.getBitNum();
        f = val ? BitUtils.setBit(f, bitNum) : BitUtils.resetBit(f, bitNum);
    }

    @Override
    public boolean getFlag(Flag flag) {
        return BitUtils.isBitSet(f, flag.getBitNum());
    }
}
