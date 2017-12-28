package interrupts;

import core.BitUtils;
import core.TestWithTestModule;
import mmu.MMU;
import org.junit.Test;

public class TestInterrupts extends TestWithTestModule {

    private MMU mmu;
    private InterruptManager interruptManager;
    private static final int INTERRUPT_REQUEST_REGISTER_ADDRESS = InterruptManager.INTERRUPT_REQUEST_REGISTER;
    private static final int INTERRUPT_ENABLE_REGISTER_ADDRESS = InterruptManager.INTERRUPT_ENABLE_REGISTER;

    public TestInterrupts() {
        mmu = testInjector.getInstance(MMU.class);
        interruptManager = testInjector.getInstance(InterruptManager.class);
    }

    @Test
    public void testInterruptBits() {
        assert (Interrupt.VBLANK.getBitNum() == 0);
        assert (Interrupt.LCD.getBitNum() == 1);
        assert (Interrupt.TIMER.getBitNum() == 2);
        assert (Interrupt.JOYPAD.getBitNum() == 4);
    }

    @Test
    public void testInterruptServiceAddress() {
        assert (Interrupt.VBLANK.getServiceAddress() == 0x40);
        assert (Interrupt.LCD.getServiceAddress() == 0x48);
        assert (Interrupt.TIMER.getServiceAddress() == 0x50);
        assert (Interrupt.JOYPAD.getServiceAddress() == 0x60);
    }

    @Test
    public void testRequestInterrupts() {
        assert (mmu.read(INTERRUPT_REQUEST_REGISTER_ADDRESS) == 0x00);
        for (Interrupt interrupt : Interrupt.values()) {
            interruptManager.requestInterrupt(interrupt);
            assert (BitUtils.isBitSet(mmu.read(INTERRUPT_REQUEST_REGISTER_ADDRESS), interrupt.getBitNum()));
        }

        assert (interruptManager.getPendingInterrupt() == null);

        interruptManager.setInterruptsEnabled(true);

        assert (interruptManager.getPendingInterrupt() == null);

        enableInterrupt(Interrupt.VBLANK);
        enableInterrupt(Interrupt.LCD);
        enableInterrupt(Interrupt.TIMER);
        enableInterrupt(Interrupt.JOYPAD);

        assert (interruptManager.getPendingInterrupt() == Interrupt.VBLANK);
        assert (!BitUtils.isBitSet(mmu.read(INTERRUPT_REQUEST_REGISTER_ADDRESS), Interrupt.VBLANK.getBitNum()));

        assert (interruptManager.getPendingInterrupt() == Interrupt.LCD);
        assert (!BitUtils.isBitSet(mmu.read(INTERRUPT_REQUEST_REGISTER_ADDRESS), Interrupt.LCD.getBitNum()));

        assert (interruptManager.getPendingInterrupt() == Interrupt.TIMER);
        assert (!BitUtils.isBitSet(mmu.read(INTERRUPT_REQUEST_REGISTER_ADDRESS), Interrupt.TIMER.getBitNum()));

        assert (interruptManager.getPendingInterrupt() == Interrupt.JOYPAD);

        interruptManager.setInterruptsEnabled(false);
        assert (interruptManager.getPendingInterrupt() == null);
    }

    private void enableInterrupt(Interrupt interrupt) {
        int interruptEnableRegister = mmu.read(INTERRUPT_ENABLE_REGISTER_ADDRESS);
        mmu.write(INTERRUPT_ENABLE_REGISTER_ADDRESS, BitUtils.setBit(interruptEnableRegister, interrupt.getBitNum()));
    }
}
