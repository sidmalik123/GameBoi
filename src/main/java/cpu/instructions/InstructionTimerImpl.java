package cpu.instructions;

import cpu.UnknownInstructionException;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete class implementing InstructionTimer
 * */
public class InstructionTimerImpl implements InstructionTimer {

    private Map<Integer, Integer> instructionToCCMap;

    public InstructionTimerImpl() {
        instructionToCCMap = new HashMap<>();
        initInstructionToCCMap();
    }

    @Override
    public int getNumCycles(int instruction) {
        Integer numCycles = instructionToCCMap.get(instruction);
        if (numCycles == null)
            throw new UnknownInstructionException("Instruction " + Integer.toHexString(instruction) + " not timed");

        return numCycles;
    }

    /**
     * Initializes instructionToCCMap
     * */
    private void initInstructionToCCMap() {
        instructionToCCMap.put(0x00, 4);
    }
}
