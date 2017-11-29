package gpu;

import gpu.palette.GBPalette;

/**
 * Interface to manage a Game Boy screen
 * */
public interface GBScreen {

    /**
     * Sets the pixel at pos(xPos, yPos) to have color color
     * Note: this does not render to the screen to see this
     * set in effect screen must be rendered by calling render()
     * */
    void setPixel(int xPos, int yPos, GBPalette.Color color);

    /**
     * Returns the color of pixel at (xPos, yPos)
     * */
    GBPalette.Color getPixelColor(int xPos, int yPos);

    /**
     * Renders the screen
     * */
    void render();
}
