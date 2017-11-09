package cpu;

public interface GBRegisterManager {

    public enum RegisterType {A, B, C, D, E, H, L, AF, BC, DE, HL};

    int get(RegisterType registerType);
    void set(RegisterType registerType, int data);
}
