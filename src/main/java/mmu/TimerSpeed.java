package mmu;

public enum  TimerSpeed {
    SPEED1, SPEED2, SPEED3, SPEED4;

    public int getFrequency() {
        switch (this) {
            case SPEED1:
                return 4096;
            case SPEED2:
                return 262144;
            case SPEED3:
                return 65536;
            case SPEED4:
                return 16384;
            default:
                throw new IllegalArgumentException("Illegal Speed passed: " + this);
        }

    }

}
