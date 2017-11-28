package gpu;

import gpu.palette.GBPalette;

/**
 * Interface to manage a GBScreen object
 * */
public interface GBScreen {

    /**
     * Sets the pixel at pos(xPos, yPos) to have color color
     * Note: this does not render to the screen to see this
     * set in effect screen must be rendered by calling render()
     * */
    void setPixel(int xPos, int yPos, GBPalette.Color color);

    /**
     * Renders the screen
     * */
    void render();
}
