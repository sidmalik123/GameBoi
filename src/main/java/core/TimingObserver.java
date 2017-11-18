package core;

public interface TimingObserver {

    /*
    * @param - numCycles = num cycles elapsed since last notify
    * */
    void notifyNumCycles(int numCycles);
}
