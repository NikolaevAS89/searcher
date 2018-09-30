package ru.timestop.entrance.service;

import ru.timestop.entrance.exception.ResultServiceException;
import ru.timestop.entrance.generated.Result;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 30.09.2018
 */
public interface ResultService {
    /**
     * search files that contains number
     *
     * @param number searched number
     * @return result of search
     */
    Result getResult(Integer number);
}
