package interrupts;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import core.BitUtils;
import mmu.MMU;

import java.util.LinkedList;
import java.util.List;

import static mmu.MMU.INTERRUPT_ENABLE_REGISTER;
import static mmu.MMU.INTERRUPT_REQUEST_REGISTER;

/**
 * Concrete class for InterruptManager
 * */
@Singleton
public class InterruptManagerImpl implements InterruptManager {

    private boolean areInterruptsEnabled;
    private MMU mmu;

    @Inject
    public InterruptManagerImpl(MMU mmu) {
        this.mmu = mmu;
    }

    @Override
    public void setInterruptsEnabled(boolean areInterruptsEnabled) {
        this.areInterruptsEnabled = areInterruptsEnabled;
    }

    @Override
    public void requestInterrupt(Interrupt interrupt) {
        mmu.write(INTERRUPT_REQUEST_REGISTER, BitUtils.setBit(mmu.read(INTERRUPT_REQUEST_REGISTER), interrupt.getBitNum()));
    }

    @Override
    public Interrupt getPendingInterrupt() {
        if (areInterruptsEnabled) {
            int interruptRequestRegister = mmu.read(INTERRUPT_REQUEST_REGISTER);
            int interruptEnableRegister = mmu.read(INTERRUPT_ENABLE_REGISTER);
            for (Interrupt interrupt : Interrupt.values()) {
                if (BitUtils.isBitSet(interruptRequestRegister, interrupt.getBitNum()) // if requested and enabled
                        && BitUtils.isBitSet(interruptEnableRegister, interrupt.getBitNum())) {
                    resetRequest(interrupt); // reset request bit
                    return interrupt;
                }
            }
        }
        return null;
    }

    private void resetRequest(Interrupt interrupt) {
        mmu.write(INTERRUPT_REQUEST_REGISTER, BitUtils.resetBit(mmu.read(INTERRUPT_REQUEST_REGISTER), interrupt.getBitNum()));
    }
}
