package cpu.interrupts;

import java.util.ArrayList;
import java.util.List;

public enum InterruptType {
    VBLANK, LCD, TIMER, SERIAL, JOYPAD;

    public int getRequestBit() {
        switch (this) {
            case VBLANK:
                return 0;
            case LCD:
                return 1;
            case TIMER:
                return 2;
            case SERIAL:
                return 3;
            case JOYPAD:
                return 4;
            default:
                throw new IllegalArgumentException("Illegal InterrupType " + this);
        }
    }

    public int getPriority() {
        switch (this) {
            case VBLANK:
                return 4;
            case LCD:
                return 3;
            case TIMER:
                return 2;
            case SERIAL:
                return 1;
            case JOYPAD:
                return 0;
            default:
                throw new IllegalArgumentException("Illegal InterrupType " + this);
        }
    }

    public static List<InterruptType> getListByPriority() {
        ArrayList<InterruptType> list = new ArrayList<InterruptType>();
        list.add(VBLANK);
        list.add(LCD);
        list.add(TIMER);
        list.add(SERIAL);
        list.add(JOYPAD);
        return list;
    }
}
