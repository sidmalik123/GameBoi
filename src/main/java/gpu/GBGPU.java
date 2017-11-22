package gpu;

import core.TimingObserver;

import java.util.List;


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
    void enableLCDInterrupts(List<GPUModeType> modeTypes);

    /**
     *  Sets the coincidence line num for the GPU,
     *  see page 52 of the manual
     * */
    void setCoincidenceLineNum(int coincidenceLineNum);

    /**
     * Enables/Disables LCD interrupt when a line coincidence occurs
     * */
    void setCoincidenceLCDInterruptEnabled(boolean isCoincidenceLCDInterruptEnabled);
}
