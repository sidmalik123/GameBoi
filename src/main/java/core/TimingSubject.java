package core;

public interface TimingSubject {

    void notifyTimingObservers(int numCycles);

    void attachTimingObserver(TimingObserver observer);

    void detachTimingObserver(TimingObserver observer);
}
