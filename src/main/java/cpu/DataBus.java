package cpu;

/**
 * Represents a data bus in GameBoy's CPU
 * */
public class DataBus {

    int data; // the data that is present in this bus

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
