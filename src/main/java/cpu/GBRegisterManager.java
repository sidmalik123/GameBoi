package cpu;

/**
 * Interface to manage register values
 * */
public interface GBRegisterManager {

    int get(SingleRegister singleRegister);
    void set(SingleRegister singleRegister, int data);

    int get(DoubleRegister doubleRegister);
    void set(DoubleRegister doubleRegister, int data);

    int getHigh(DoubleRegister doubleRegister);
    int getLow(DoubleRegister doubleRegister);

    void setHigh(DoubleRegister doubleRegister, int data);
    void setLow(DoubleRegister doubleRegister, int data);

    void setZeroFlag(boolean bool);
    boolean getZeroFlag();

    void setOperationFlag(boolean bool);
    boolean getOperationFlag();

    void setCarryFlag(boolean bool);
    boolean getCarryFlag();

    void setHalfCarryFlag(boolean bool);
    boolean getHalfCarryFlag();
}
