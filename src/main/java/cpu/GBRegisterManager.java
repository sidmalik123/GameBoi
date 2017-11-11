package cpu;

public interface GBRegisterManager {

    int get(RegisterType registerType);
    void set(RegisterType registerType, int data);
}
