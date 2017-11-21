package cpu.interrupts;


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
                throw new IllegalArgumentException("Illegal InterruptType " + this);
        }
    }

    public int getServiceAddress() {
        switch (this) {
            case VBLANK:
                return 0x40;
            case LCD:
                return 0x48;
            case TIMER:
                return 0x50;
            case SERIAL:
                return 0x58;
            case JOYPAD:
                return 0x60;
            default:
                throw new IllegalArgumentException("Illegal InterruptType " + this);
        }
    }
}
