package cpu;

/**
 * Represents the Program counter
 * */
public class ProgramCounter {

    private int value;

    public ProgramCounter(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Increments pc by 1
     */
    public void inc() {
        ++value;
    }
}
