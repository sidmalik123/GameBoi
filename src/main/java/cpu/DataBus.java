package cpu;

/**
 * Represents a data bus in GameBoy's CPU
 * */
public class DataBus {

    private static final int RESET_VALUE = 0;
    private int data; // the data that is present in this bus

    public int getData() {
        return data;
    }

    public void reset() {
        this.data = RESET_VALUE;
    }

    public void setData(int data) {
        this.data = data;
    }
}
