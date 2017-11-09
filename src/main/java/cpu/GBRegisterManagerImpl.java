package cpu;

public class GBRegisterManagerImpl implements GBRegisterManager {

    // Registers
    private Register regA;
    private Register regB;
    private Register regC;
    private Register regD;
    private Register regE;
    private Register regH;
    private Register regL;
    //@todo - add the flags register



    public int get(RegisterType registerType) {
        switch (registerType) {
            case A:
                return regA.getData();
            case B:
                return regB.getData();
            case C:
                return regC.getData();
            case D:
                return regD.getData();
            case E:
                return regE.getData();
            case H:
                return regH.getData();
            case L:
                return regL.getData();
            case BC:
                return regB.getData() << 8 | regC.getData();
            case DE:
                return regD.getData() << 8 | regE.getData();
            case HL:
                return regH.getData() << 8 | regL.getData();
            default:
                throw new IllegalArgumentException("Register type " + registerType + " has not been implemented yet");

        }
    }

    public void set(RegisterType registerType, int data) {
        switch (registerType) {
            case A:
                regA.setData(data);
            case B:
                regB.setData(data);
            case C:
                regC.setData(data);
            case D:
                regD.setData(data);
            case E:
                regE.setData(data);
            case H:
                regH.setData(data);
            case L:
                regL.setData(data);
            case BC:
                regB.setData(data >> 8);
                regC.setData(data & 0xFF);
            case DE:
                regD.setData(data >> 8);
                regE.setData(data & 0xFF);
            case HL:
                regH.setData(data >> 8);
                regL.setData(data & 0xFF);
            default:
                throw new IllegalArgumentException("Register type " + registerType + " has not been implemented yet");

        }
    }
}
