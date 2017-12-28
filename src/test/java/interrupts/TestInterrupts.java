package interrupts;

import core.BitUtils;
import core.TestWithTestModule;
import mmu.MMU;
import org.junit.Test;

import java.util.List;

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
        assert (InterruptType.VBLANK.getBitNum() == 0);
        assert (InterruptType.LCD.getBitNum() == 1);
        assert (InterruptType.TIMER.getBitNum() == 2);
        assert (InterruptType.JOYPAD.getBitNum() == 4);
    }

    @Test
    public void testInterruptServiceAddress() {
        assert (InterruptType.VBLANK.getServiceAddress() == 0x40);
        assert (InterruptType.LCD.getServiceAddress() == 0x48);
        assert (InterruptType.TIMER.getServiceAddress() == 0x50);
        assert (InterruptType.JOYPAD.getServiceAddress() == 0x60);
    }

    @Test
    public void testRequestInterrupts() {
        assert (mmu.read(INTERRUPT_REQUEST_REGISTER_ADDRESS) == 0x00);
        for (InterruptType interruptType : InterruptType.values()) {
            interruptManager.requestInterrupt(interruptType);
            assert (BitUtils.isBitSet(mmu.read(INTERRUPT_REQUEST_REGISTER_ADDRESS), interruptType.getBitNum()));
        }

        assert (interruptManager.getPendingInterrupts().isEmpty());

        interruptManager.setInterruptsEnabled(true);

        assert (interruptManager.getPendingInterrupts().isEmpty());

        enableInterrupt(InterruptType.VBLANK);

        List<InterruptType> interrupts = interruptManager.getPendingInterrupts();
        assert (interrupts.size() == 1 && interrupts.get(0) == InterruptType.VBLANK);

        enableInterrupt(InterruptType.LCD);
        interrupts = interruptManager.getPendingInterrupts();
        assert (interrupts.size() == 2 && interrupts.get(0) == InterruptType.VBLANK && interrupts.get(1) == InterruptType.LCD);

        enableInterrupt(InterruptType.TIMER);
        enableInterrupt(InterruptType.JOYPAD);
        interrupts = interruptManager.getPendingInterrupts();
        assert (interrupts.size() == 4);
        assert (interrupts.get(0) == InterruptType.VBLANK);
        assert (interrupts.get(1) == InterruptType.LCD);
        assert (interrupts.get(2) == InterruptType.TIMER);
        assert (interrupts.get(3) == InterruptType.JOYPAD);

        interruptManager.setInterruptsEnabled(false);
        assert (interruptManager.getPendingInterrupts().isEmpty());
    }

    private void enableInterrupt(InterruptType interruptType) {
        int interruptEnableRegister = mmu.read(INTERRUPT_ENABLE_REGISTER_ADDRESS);
        mmu.write(INTERRUPT_ENABLE_REGISTER_ADDRESS, BitUtils.setBit(interruptEnableRegister, interruptType.getBitNum()));
    }
}
