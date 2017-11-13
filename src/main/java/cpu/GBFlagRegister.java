package cpu;

public class GBFlagRegister {

    private boolean z; // set if the result is zero
    private boolean n; // sometimes called o, set if the operation is subtraction
    private boolean h; // halfCarry - Set if, in the result of the last operation, the lower half of the byte overflowed past 15
    private boolean c; // carry - Set if the last operation produced a result over 255 (for additions) or under 0 (for subtractions)

    public boolean isZ() {
        return z;
    }

    public void setZ(boolean z) {
        this.z = z;
    }

    public boolean isN() {
        return n;
    }

    public void setN(boolean n) {
        this.n = n;
    }

    public boolean isH() {
        return h;
    }

    public void setH(boolean h) {
        this.h = h;
    }

    public boolean isC() {
        return c;
    }

    public void setC(boolean c) {
        this.c = c;
    }
}
