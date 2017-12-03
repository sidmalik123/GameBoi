package gpu;

import gpu.palette.GBPalette;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class to implement GBScreen
 * */
public class GBScreenImpl implements GBScreen {
    private List<List<GBPalette.Color>> screenBuffer;

    public static final int NUM_ROWS = 144;
    public static final int NUM_COLS = 160;

    /**
     * Inits the 160x144 screen buffer
     * */
    public GBScreenImpl() {
        screenBuffer = new ArrayList<List<GBPalette.Color>>(NUM_COLS);
        for (int i = 0; i < NUM_ROWS; ++i) {
            List<GBPalette.Color> row = new ArrayList<GBPalette.Color>(NUM_COLS);
            screenBuffer.add(row);
        }
    }

    /**
     * Sets the color of the pixel at (xPos, yPos) in the screenBuffer
     * */
    public void setPixel(int xPos, int yPos, GBPalette.Color color) {
        screenBuffer.get(yPos).set(xPos, color);
    }

    /**
     * Returns the color of the pixel at (xPos, yPos) in the screenBuffer
     * */
    public GBPalette.Color getPixelColor(int xPos, int yPos) {
        return screenBuffer.get(yPos).get(xPos);
    }

    //Todo
    public void render() {
        for (List<GBPalette.Color> row: screenBuffer) {
            for (GBPalette.Color color : row) {
                if (color == null || color == GBPalette.Color.WHITE) {
                    System.out.print(" ");
                } else {
                    System.out.print("x");
                }
            }
            System.out.println();
        }
    }
}
