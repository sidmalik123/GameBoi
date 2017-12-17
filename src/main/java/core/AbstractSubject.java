package core;

import java.util.List;

/**
 * Abstract class to handle general Subject operations in a Subject-Observer Pattern
 *
 * @param <T> the type of Observer
 * */
public abstract class AbstractSubject<T> {

    protected List<T> observers;

    /**
     * Attaches newObserver to this subject
     * */
    void attach(T newObserver) {
        observers.add(newObserver);
    }

    /**
     * Detaches toDetach from this subject
     * */
    void detach(T toDetach) {
        observers.remove(toDetach);
    }
}
