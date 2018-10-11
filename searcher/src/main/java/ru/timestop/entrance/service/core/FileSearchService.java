package ru.timestop.entrance.service.core;

import ru.timestop.entrance.entities.AgregatedSearchResult;

import java.util.concurrent.Future;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 11.10.2018
 */
public interface FileSearchService {
    /**
     * search number in files in parallel mode
     *
     * @param number that will be searched
     * @return result of search
     */
    Future<AgregatedSearchResult> search(Integer number);
}
