package cpu;

import cpu.alu.ALU;
import cpu.clock.ClockObserver;
import cpu.clock.ClockSubject;
import cpu.instructionstage.*;
import cpu.registers.Registers;
import mmu.MMU;

import java.util.LinkedList;
import java.util.List;


public class CPUImpl extends ClockSubject implements CPU {

    private DataBus dataBus1, dataBus2;

    private List<InstructionExecuteStage> pipeline;

    public CPUImpl(MMU mmu, Registers registers, ALU alu) {
        ProgramCounter programCounter = new ProgramCounter(0);
        dataBus1 = new DataBus();
        dataBus2 = new DataBus();
        // init pipeline
        pipeline = new LinkedList<>();
        pipeline.add(new FetchStage(mmu, dataBus1, programCounter));
        pipeline.add(new ControlsGeneratorStage(dataBus1));
        pipeline.add(new RegisterReadStage(registers, dataBus1, dataBus2));
        pipeline.add(new MemoryStage(mmu, dataBus1, dataBus2, programCounter));
        pipeline.add(new ALUStage(dataBus1, dataBus2));
        pipeline.add(new RegisterWriteStage(registers, dataBus1));
    }

    @Override
    public void run() {
        while(true) {
            executeNextInstruction();
        }
    }

    private void executeNextInstruction() {
        for (InstructionExecuteStage stage : pipeline) {
            notifyClockIncrement(stage.execute());
        }
        dataBus1.reset();
        dataBus2.reset();
    }

    /**
     * Notifies all of its observers to handle clock increment
     * */
    @Override
    protected void notifyClockIncrement(int increment) {
        for (ClockObserver observer : this.observers) {
            observer.handleClockIncrement(increment);
        }
    }
}
