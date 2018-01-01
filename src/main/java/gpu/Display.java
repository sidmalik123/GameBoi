package gpu;

/**
 * GameBoy Display
 * */
public interface Display {
    /**
     * Sets the pixel at pos (x,y) to have color
     * */
    void setPixel(int x, int y, Color color);

    /**
     * Refresh Screen
     * */
    void refresh();
}
