package gpu;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * The different modes the GPU enters
 * when a CRT scan is done
 * */
enum GPUMode {

    ACCESSING_OAM {
        @Override
        public int getNumCyclesToSpend() {
            return 80;
        }

        @Override
        public int getModeNum() {
            return 2;
        }

        @Override
        public int getInterruptBitNum() {
            return 5;
        }
    }, ACCESSING_VRAM {
        @Override
        public int getNumCyclesToSpend() {
            return 172;
        }

        @Override
        public int getModeNum() {
            return 3;
        }

        @Override
        public int getInterruptBitNum() {
            throw new NotImplementedException();
        }
    }, HBLANK {
        @Override
        public int getNumCyclesToSpend() {
            return 204;
        }

        @Override
        public int getModeNum() {
            return 0;
        }

        @Override
        public int getInterruptBitNum() {
            return 3;
        }
    }, VBLANK {
        @Override
        public int getNumCyclesToSpend() {
            return 456;
        }

        @Override
        public int getModeNum() {
            return 1;
        }

        @Override
        public int getInterruptBitNum() {
            return 4;
        }
    };

    /**
     * Returns the number of CPU cycles the GPU
     * spends in a mode
     * */
    public abstract int getNumCyclesToSpend();

    public abstract int getModeNum();

    /**
     * Returns the bit for this mode in the LCD status address
     * */
    public abstract int getInterruptBitNum();
}
