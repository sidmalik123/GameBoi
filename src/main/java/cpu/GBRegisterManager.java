package cpu;

public interface GBRegisterManager {

    int get(SingleRegister singleRegister);
    void set(SingleRegister singleRegister, int data);

    int get(DoubleRegister doubleRegister);
    void set(DoubleRegister doubleRegister, int data);

    int getHigh(DoubleRegister doubleRegister);
    int getLow(DoubleRegister doubleRegister);

    void setHigh(DoubleRegister doubleRegister, int data);
    void setLow(DoubleRegister doubleRegister, int data);
}
