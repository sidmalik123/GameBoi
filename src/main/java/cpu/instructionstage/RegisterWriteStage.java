package cpu.instructionstage;

import cpu.DataBus;
import cpu.registers.Registers;

/**
 * In this stage register writes are done
 * */
public class RegisterWriteStage implements InstructionExecuteStage {

    private DataBus dataBus1;

    private Registers.Register registerToWriteTo;

    private Registers registers;

    public RegisterWriteStage(Registers registers, DataBus dataBus1) {
        this.registers = registers;
        this.dataBus1 = dataBus1;
    }

    public void setRegisterToWriteTo(Registers.Register registerToWriteTo) {
        this.registerToWriteTo = registerToWriteTo;
    }

    @Override
    public int execute() {
        if (registerToWriteTo != null) registers.write(registerToWriteTo, dataBus1.getData());
        registerToWriteTo = null; // reset
        return 0;
    }
}
