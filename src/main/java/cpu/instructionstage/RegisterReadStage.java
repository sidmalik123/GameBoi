package cpu.instructionstage;

import cpu.DataBus;
import cpu.registers.Registers;

/**
 * Represent the stage where register reads happen
 * */
public class RegisterReadStage implements InstructionExecuteStage {

    private Registers.Register firstRegisterToRead, secondRegisterToRead;

    private DataBus dataBus1, dataBus2;

    private Registers registers;

    public RegisterReadStage(Registers registers, DataBus dataBus1, DataBus dataBus2) {
        this.registers = registers;
        this.dataBus1 = dataBus1;
        this.dataBus2 = dataBus2;
    }

    public void setFirstRegisterToRead(Registers.Register firstRegisterToRead) {
        this.firstRegisterToRead = firstRegisterToRead;
    }

    public void setSecondRegisterToRead(Registers.Register secondRegisterToRead) {
        this.secondRegisterToRead = secondRegisterToRead;
    }

    @Override
    public int execute() {
        if (firstRegisterToRead != null) dataBus1.setData(registers.read(firstRegisterToRead));
        if (secondRegisterToRead != null) dataBus2.setData(registers.read(secondRegisterToRead));

        firstRegisterToRead = null; secondRegisterToRead = null; // reset register options
        return  0;
    }
}
