package cpu;

import java.util.Map;

public class GBRegisterManagerImpl implements GBRegisterManager {

    private Map<SingleRegister, Register> registerMap; // for registers A,B,C,D,E,H,L
    private GBFlagRegister flagRegister;
    private int stackPointer;

    public GBRegisterManagerImpl() {
        for (SingleRegister r : SingleRegister.values()) {
            registerMap.put(r, new Register());
        }
    }

    public int get(SingleRegister singleRegister) {
        return registerMap.get(singleRegister).getData();
    }

    public void set(SingleRegister singleRegister, int data) {
        registerMap.get(singleRegister).setData(data);
    }

    // Todo - refactor to have double register retunr
    public int get(DoubleRegister doubleRegister) {
        if (doubleRegister == DoubleRegister.SP) return stackPointer;
        return registerMap.get(doubleRegister.getHighRegister()).getData() << 8 |
                registerMap.get(doubleRegister.getLowRegister()).getData();
    }

    public void set(DoubleRegister doubleRegister, int data) {
        if (doubleRegister == DoubleRegister.SP) {
            stackPointer = data;
            return;
        }
        registerMap.get(doubleRegister.getHighRegister()).setData(data >> 8);
        registerMap.get(doubleRegister.getLowRegister()).setData(data & 0xFF);
    }

    public void set(SingleRegister r1, SingleRegister r2) {
        set(r1, get(r2));
    }

    public void set(DoubleRegister d1, DoubleRegister d2) {
        set(d1, get(d2));
    }

    public int getHigh(DoubleRegister doubleRegister) {
        return registerMap.get(doubleRegister.getHighRegister()).getData();
    }

    public int getLow(DoubleRegister doubleRegister) {
        return registerMap.get(doubleRegister.getLowRegister()).getData();
    }

    public void setHigh(DoubleRegister doubleRegister, int data) {
        registerMap.get(doubleRegister.getHighRegister()).setData(data);
    }

    public void setLow(DoubleRegister doubleRegister, int data) {
        registerMap.get(doubleRegister.getLowRegister()).setData(data);
    }

    public void setZeroFlag(boolean bool) {
        flagRegister.setZ(bool);
    }

    public boolean getZeroFlag() {
        return flagRegister.isZ();
    }

    public void setOperationFlag(boolean bool) {
        flagRegister.setN(bool);
    }

    public boolean getOperationFlag() {
        return flagRegister.isZ();
    }

    public void setCarryFlag(boolean bool) {
        flagRegister.setC(bool);
    }

    public boolean getCarryFlag() {
        return flagRegister.isC();
    }

    public void setHalfCarryFlag(boolean bool) {
        flagRegister.setH(bool);
    }

    public boolean getHalfCarryFlag() {
        return flagRegister.isH();
    }
}
