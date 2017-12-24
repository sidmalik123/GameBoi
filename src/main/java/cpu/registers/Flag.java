package cpu.registers;

/**
 * There are four flags in the Gameboy Z80:
 * */
public enum Flag {

    ZERO {
        @Override
        public int getBitNum() {
            return 7;
        }
    }, SUBTRACTION {
        @Override
        public int getBitNum() {
            return 6;
        }
    }, HALF_CARRY {
        @Override
        public int getBitNum() {
            return 5;
        }
    }, CARRY {
        @Override
        public int getBitNum() {
            return 4;
        }
    };

    /**
     * Returns the bit that represents this flag in Register F
     */
    public abstract int getBitNum();
}
