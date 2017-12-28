package interrupts;

/**
 * These represent the different interrupts in GameBoy
 *
 * Note: these are ordered below by priority
 * */
public enum InterruptType {
    VBLANK {
        @Override
        public int getBitNum() {
            return 0;
        }

        @Override
        public int getServiceAddress() {
            return 0x40;
        }
    },
    LCD {
        @Override
        public int getBitNum() {
            return 1;
        }

        @Override
        public int getServiceAddress() {
            return 0x48;
        }
    },
    TIMER {
        @Override
        public int getBitNum() {
            return 2;
        }

        @Override
        public int getServiceAddress() {
            return 0x50;
        }
    },
    JOYPAD {
        @Override
        public int getBitNum() {
            return 4;
        }

        @Override
        public int getServiceAddress() {
            return 0x60;
        }
    };

    /**
     * Returns its bit num in the interrupt request register and interrupt enable register
     * */
    public abstract int getBitNum();

    /**
     * Returns the address that the cpu must jump to service this interrupt
     * */
    public abstract int getServiceAddress();
}
