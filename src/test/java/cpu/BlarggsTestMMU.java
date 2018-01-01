package cpu;

import com.google.inject.Singleton;
import mmu.MMU;
import mmu.MMUImpl;
import mmu.cartridge.Cartridge;

/**
 * Used in blargg's test, prints test results to stdout
 * */
@Singleton
public class BlarggsTestMMU implements MMU {

    private MMU mmu;
    private StringBuilder stringBuilder;

    public BlarggsTestMMU() {
        this.mmu = new MMUImpl();
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public int read(int address) {
        return mmu.read(address);
    }

    @Override
    public void write(int address, int data) {
        if (address == 0xFF02 && data == 0x81) { // printing logic
            char c = (char) read(0xFF01);
            stringBuilder.append(c);
        }

        mmu.write(address, data);
    }

    @Override
    public void load(Cartridge cartridge) {
        mmu.load(cartridge);
    }

    @Override
    public void setCurrLineNum(int lineNum) {
        mmu.setCurrLineNum(lineNum);
    }

    public String getTestOutput() {
        return stringBuilder.toString();
    }
}
