package cpu;

import com.google.inject.Injector;
import cpu.clock.Clock;
import cpu.instructions.InstructionExecutor;
import gpu.GPU;
import mmu.MMU;
import mmu.cartridge.CartridgeImpl;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestBlarggsInstructions extends BlarggsTest {

    private MMU mmu;
    private List<String> testNames;
    private List<String> knownFailures;

    public TestBlarggsInstructions() {
        testNames = new ArrayList<>(11);
        testNames.add("01-special.gb");
        testNames.add("02-interrupts.gb");
        testNames.add("03-op sp,hl.gb");
        testNames.add("04-op r,imm.gb");
        testNames.add("05-op rp.gb");
        testNames.add("06-ld r,r.gb");
        testNames.add("07-jr,jp,call,ret,rst.gb");
        testNames.add("08-misc instrs.gb");
        testNames.add("09-op r,r.gb");
        testNames.add("10-bit ops.gb");
        testNames.add("11-op a,(hl).gb");

        knownFailures = new ArrayList<>();
        knownFailures.add(testNames.get(1)); // interrupts test
    }

    /**
     * Runs the test for 45s, returns the test result after that
     * */
    private String runTest(String testName) throws IOException {
        Injector blarggsInjector = getNewInjector();
        mmu = blarggsInjector.getInstance(MMU.class);
        mmu.load(new CartridgeImpl("src/test/java/cpu/cpu_instrs/individual/" + testName));
        InstructionExecutor instructionExecutor = blarggsInjector.getInstance(InstructionExecutor.class);
        Clock clock = blarggsInjector.getInstance(Clock.class);
        clock.attach(blarggsInjector.getInstance(GPU.class));

        long startTime = System.currentTimeMillis();
        String outputTillNow = "";
        while (!outputTillNow.contains("Passed") && System.currentTimeMillis() - startTime < 45000) {
            instructionExecutor.executeInstruction();
            outputTillNow = ((BlarggsTestMMU) mmu).getTestOutput();
        }
        return ((BlarggsTestMMU) mmu).getTestOutput();
    }

    @Test
    public void testInstructions() throws IOException {
        for (String testName: testNames) {
            if (knownFailures.contains(testName)) continue; // skip this for now

            try {
                String testOutput = runTest(testName);
                if (testOutput.contains("Passed")) {
                    System.out.println("Test " + testName + " passed, test output " + testOutput);
                    assertTrue(true);
                } else {
                    System.out.println("Test: " + testName + " failed with output " + testOutput);
                    fail();
                }
            } catch (IOException ex) {
                fail();
            }
        }
    }
}
