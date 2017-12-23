package mmu;

import gpu.Display;
import gpu.DisplayImpl;
import gpu.GPU;
import gpu.GPUImpl;
import mmu.memoryspaces.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MMUFactoryImpl implements MMUFactory {

    private static final int ZERO_PAGE_START_ADDRESS = 0xFF80;
    private static final int ZERO_PAGE_END_ADDRESS = 0xFFFF;

    @Override
    public MMU create() {
        List<MemorySpace> memorySpaces = new ArrayList<>();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame mainWindow = new JFrame("GameBoi");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);

        DisplayImpl display = new DisplayImpl();
        display.setSize(160, 144);

        mainWindow.setContentPane(display);
        mainWindow.setResizable(true);
        mainWindow.setVisible(true);
        mainWindow.setSize(160, 144);

        GPU gpu = new GPUImpl(display);
        memorySpaces.add(new ROM());
        memorySpaces.add(new RAM());
        memorySpaces.add(gpu);
        memorySpaces.add(new IOMemory());
        memorySpaces.add(new ContinuousMemorySpace(ZERO_PAGE_START_ADDRESS, ZERO_PAGE_END_ADDRESS));
        MMUImpl mmu = new MMUImpl(memorySpaces);
        mmu.attach(gpu);

        return mmu;
    }
}
