package gpu;

import cpu.clock.ClockObserver;

/**
 * GameBoy's Graphic Processing Unit,
 *
 * For the purpose of making code cleaner, VRAM and OAM (Sprite memory),
 * which are separate parts of memory in reality (that GPU reads from)
 * is modelled to be contained in GPU
 * */
public interface GPU extends ClockObserver {
    int WIDTH = 160; // 160 px
    int HEIGHT = 144; // 144 px
}
