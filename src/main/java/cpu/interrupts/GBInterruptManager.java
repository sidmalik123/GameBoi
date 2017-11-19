package cpu.interrupts;

import core.BitUtils;
import mmu.GBMMU;

public class GBInterruptManager implements InterruptManager {

    private boolean interruptsEnabled; /* set/reset by cpu ops */

    private GBMMU mmu;

    public void requestInterrupt(InterruptType interruptType) {
        int requestAddressVal = mmu.getInterruptRequestAddress();
        requestAddressVal = BitUtils.setBit(interruptType.getRequestBit());
        mmu.writeData(mmu.getInterruptRequestAddress(), requestAddressVal);
    }

    private boolean isInterruptsEnabled() {
        return interruptsEnabled;
    }

    public InterruptType getCurrentInterrupt() {
        if (!isInterruptsEnabled()) return null;

        for (InterruptType interruptType : InterruptType.getListByPriority()) {
            if (isRequested(interruptType) && isEnabled(interruptType))
                return interruptType;
        }

        return null;
    }

    private boolean isRequested(InterruptType interruptType) {
        int requestAddressVal = mmu.getInterruptRequestAddress();
        return BitUtils.isBitSet(requestAddressVal, interruptType.getRequestBit());
    }

    private boolean isEnabled(InterruptType interruptType) {
        int interruptEnableAddressVal = mmu.getInterruptEnableAddress();
        return BitUtils.isBitSet(interruptEnableAddressVal, interruptType.getRequestBit());
    }

    public void setInterruptsEnabled(boolean interruptsEnabled) {
        this.interruptsEnabled = interruptsEnabled;
    }
}
