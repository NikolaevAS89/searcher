package ru.timestop.entrance.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * synthetic class for aggregate results of search task
 *
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 10.10.2018
 */
public class AgregatedSearchResult {
    private List<String> files;
    private List<Throwable> exceptions;

    public AgregatedSearchResult() {
        files = new ArrayList<>();
        exceptions = new ArrayList<>();
    }

    public List<String> getFiles() {
        return files;
    }

    public void addFile(String file) {
        this.files.add(file);
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    public void addException(Throwable exception) {
        this.exceptions.add(exception);
    }
}
