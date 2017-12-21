package gpu;

import cpu.clock.ClockObserver;
import mmu.memoryspaces.MemorySpace;

/**
 * GameBoy's Graphic Processing Unit,
 *
 * For the purpose of making code cleaner, VRAM and OAM (Sprite memory),
 * which are separate parts of memory in reality (that GPU reads from)
 * is modelled to be contained in GPU
 * */
public interface GPU extends MemorySpace, ClockObserver {
}
