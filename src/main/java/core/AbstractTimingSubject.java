package core;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTimingSubject implements TimingSubject {

    protected List<TimingObserver> observers;

    public AbstractTimingSubject() {
        observers = new ArrayList<TimingObserver>();
    }

    public void attachTimingObserver(TimingObserver observer) {
        if (!observers.contains(observer))
            observers.add(observer);
    }

    public void detachTimingObserver(TimingObserver observer) {
        observers.remove(observer);
    }
}
