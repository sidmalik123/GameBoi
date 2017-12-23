package core;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class to handle general Subject operations in a Subject-Observer Pattern
 *
 * @param <T> the type of Observer
 * */
public abstract class AbstractSubject<T> {

    protected List<T> observers = new ArrayList<T>();

    /**
     * Attaches newObserver to this subject
     * */
    public void attach(T newObserver) {
        observers.add(newObserver);
    }

    /**
     * Detaches toDetach from this subject
     * */
    public void detach(T toDetach) {
        observers.remove(toDetach);
    }
}
