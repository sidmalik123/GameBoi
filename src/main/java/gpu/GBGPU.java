package gpu;

import core.TimingObserver;
import gpu.palette.GBPalette;


/**
 * Interface for a GameBoy GPU unit
 * */
public interface GBGPU extends TimingObserver {

    /**
     *  Enables/Disabled the LCD
     * */
    void setLCDEnabled(boolean isEnabled);


    /**
     * Returns the mode the GPU is currently in
     * */
    GPUModeType getCurrentMode();

    /**
     *  Enables LCD Interrupt for all GPUModeTypes in modeTypes
     * */
    void setLCDInterrupt(GPUModeType modeType, boolean isEnabled);

    /**
     *  Sets the coincidence line num for the GPU,
     *  see page 52 of the manual
     * */
    void setCoincidenceLineNum(int coincidenceLineNum);

    /**
     * Enables/Disables LCD interrupt when a line coincidence occurs
     * */
    void setCoincidenceLCDInterruptEnabled(boolean isCoincidenceLCDInterruptEnabled);

    /**
     *  Checks if background is enabled or not
     * */
    void setBackgroundEnabled(boolean isEnabled);

    /**
     * Checks if sprites are enabled or not
     * */
    void setSpritesEnabled(boolean isEnabled);

    /**
     * Checks if window is enabled or not
     * */
    void setWindowEnabled(boolean isEnabled);

    /**
     * Sets the top left x coordinate of the background
     * */
    void setBackgroundScrollX(int scrollX);

    /**
     * Sets the top left y coordinate of the background
     * */
    void setBackgroundScrollY(int scrollY);

    /**
     * Sets the top left x coordinate of the window
     * */
    void setWindowScrollX(int scrollX);

    /**
     * Sets the top left y coordinate of the window
     * */
    void setWindowScrollY(int scrollY);

    /***
     * Sets background palette's color mapping
     */
    void setBackgroundPalette(GBPalette palette);

    /**
     * Returns the current line number the GPU is rendering
     * */
    int getCurrLineNum();

    /**
     * Sets the first palette for Sprites
     * */
    void setSpritesPalette1(GBPalette palette);

    /**
     * Sets the second palette for Sprites
     * */
    void setSpritesPalette2(GBPalette palette);
}
