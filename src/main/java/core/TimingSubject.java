package core;

public interface TimingSubject {

    /**
     * Notify observers with numCycles since last notify
     * */
    void notifyTimingObservers(int numCycles);

    void attachTimingObserver(TimingObserver observer);

    void detachTimingObserver(TimingObserver observer);
}
