package ru.timestop.entrance.service;

import ru.timestop.entrance.generated.Result;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 01.10.2018
 */
public interface SearchService {

    /**
     * search files that contains number
     *
     * @param number searched number
     * @return result of search
     */
    Result search(Integer number);
}
