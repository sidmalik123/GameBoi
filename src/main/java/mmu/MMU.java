package mmu;

public interface MMU {

    /**
     * Reads Data at the address passed in
     * @return data read
     * */
    int readData(int address);


    /**
     * Writes data passed in at the address passed in
     * @param address address to write to
     * @param data data to write
     * */
    void writeData(int address, int data);

    /**
     * loads program at programLocation in memory
     * @param programLocation path to program file
     * @return programCounter after load
     * */
    int loadProgram(String programLocation);

}
