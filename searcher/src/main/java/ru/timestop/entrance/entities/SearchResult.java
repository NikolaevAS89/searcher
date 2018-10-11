package ru.timestop.entrance.entities;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 10.10.2018
 */
public class SearchResult {
    private volatile boolean isComputed = false;
    private boolean isFound = false;

    public boolean isComputed() {
        return isComputed;
    }

    public boolean isFound() {
        return isFound;
    }

    public synchronized void setFound() {
        isComputed = true;
        isFound = true;
    }

    public synchronized void setNotFound() {
        isComputed = true;
    }
}
