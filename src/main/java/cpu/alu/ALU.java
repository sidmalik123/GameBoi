package cpu.alu;

/**
 * Interface to represent a GameBoy's arithmetic logic unit
 * */
public interface ALU {

    /**
     * Returns word formed by joining highByte and lowByte
     * */
    int joinBytes(int highByte, int lowByte);
}
