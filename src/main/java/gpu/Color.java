package gpu;

/**
 * These are the colors a GameBoy can display
 * */
public enum Color {
    WHITE {
        @Override
        public int getRGB() {
            return java.awt.Color.WHITE.getRGB();
        }

        @Override
        public int getColorNum() {
            return 0;
        }
    }, LIGHT_GRAY {
        @Override
        public int getRGB() {
            return java.awt.Color.LIGHT_GRAY.getRGB();
        }

        @Override
        public int getColorNum() {
            return 1;
        }
    }, DARK_GRAY {
        @Override
        public int getRGB() {
            return java.awt.Color.DARK_GRAY.getRGB();
        }

        @Override
        public int getColorNum() {
            return 3;
        }
    }, BLACK {
        @Override
        public int getRGB() {
            return java.awt.Color.BLACK.getRGB();
        }

        @Override
        public int getColorNum() {
            return 4;
        }
    };

    /**
     * Returns the RGB value for this color
     * */
    public abstract int getRGB();

    /**
     * This is the color number 1-3
     * */
    public abstract int getColorNum();
}
