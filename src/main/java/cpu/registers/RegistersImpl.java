package cpu.registers;

import core.BitUtils;

/**
 * Concrete class to implement Registers
 * */
public class RegistersImpl implements Registers {

    // int is sufficient to represent a register
    private int a, b, c, d, e, f, h, l, sp;

    private static int ZERO_FLAG_BIT = 7;
    private static int SUBTRACTION_FLAG_BIT = 6;
    private static int HALF_CARRY_BIT = 5;
    private static int CARRY_BIT = 4;

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
        if (data < 0) System.out.println("negative data : " + data);
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
        }
    }

    @Override
    public void setFlag(Flag flag, boolean val) {
        final int bitNum;
        switch (flag) {
            case ZERO: bitNum = ZERO_FLAG_BIT; break;
            case SUBTRACTION: bitNum = SUBTRACTION_FLAG_BIT; break;
            case HALF_CARRY: bitNum = HALF_CARRY_BIT; break;
            case CARRY: bitNum = CARRY_BIT; break;
            default: throw new IllegalArgumentException("Unhandled flag " + flag);
        }
        f = val ? BitUtils.setBit(f, bitNum) : BitUtils.resetBit(f, bitNum);
    }

    @Override
    public boolean getFlag(Flag flag) {
        switch (flag) {
            case ZERO: return BitUtils.isBitSet(f, ZERO_FLAG_BIT);
            case SUBTRACTION: return BitUtils.isBitSet(f, SUBTRACTION_FLAG_BIT);
            case HALF_CARRY: return BitUtils.isBitSet(f, HALF_CARRY_BIT);
            case CARRY: return BitUtils.isBitSet(f, CARRY_BIT);
            default: throw new IllegalArgumentException("Unhandled flag " + flag);
        }
    }
}
