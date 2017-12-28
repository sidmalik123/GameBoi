package interrupts;

import com.google.inject.Singleton;
import core.BitUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Concrete class for InterruptManager
 * */
@Singleton
public class InterruptManagerImpl implements InterruptManager {

    private boolean areInterruptsEnabled;

    private int interruptRequestRegister;
    private int interruptEnableRegister;

    @Override
    public void setInterruptsEnabled(boolean areInterruptsEnabled) {
        this.areInterruptsEnabled = areInterruptsEnabled;
    }

    @Override
    public void requestInterrupt(InterruptType interruptType) {
        interruptRequestRegister = BitUtils.setBit(interruptRequestRegister, interruptType.getBitNum());
    }

    @Override
    public List<InterruptType> getPendingInterrupts() {
        List<InterruptType> pendingInterrupts = new LinkedList<>();
        if (areInterruptsEnabled) {
            for (InterruptType interruptType : InterruptType.values()) {
                if (BitUtils.isBitSet(interruptRequestRegister, interruptType.getBitNum()) // if requested and enabled
                        && BitUtils.isBitSet(interruptEnableRegister, interruptType.getBitNum())) {
                    pendingInterrupts.add(interruptType);
                }
            }
        }
        return pendingInterrupts;
    }

    @Override
    public boolean accepts(int address) {
        return address == INTERRUPT_REQUEST_REGISTER || address == INTERRUPT_ENABLE_REGISTER;
    }

    @Override
    public int read(int address) {
        if (address == INTERRUPT_REQUEST_REGISTER) { return interruptRequestRegister; }
        else if (address == INTERRUPT_ENABLE_REGISTER) { return interruptEnableRegister; }
        else { throw new IllegalArgumentException("Address 0x" + Integer.toHexString(address) + " is not in this memory space"); }
    }

    @Override
    public void write(int address, int data) {
        if (address == INTERRUPT_REQUEST_REGISTER) { interruptRequestRegister = data; }
        else if (address == INTERRUPT_ENABLE_REGISTER) { interruptEnableRegister = data; }
        else { throw new IllegalArgumentException("Address 0x" + Integer.toHexString(address) + " is not in this memory space"); }
    }
}
