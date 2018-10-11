package ru.timestop.entrance.service.facade;

import ru.timestop.entrance.generated.Result;

/**
 * Facade for service
 *
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 01.10.2018
 */
public interface ResultService {

    /**
     * getResult files that contains number
     *
     * @param number searched number
     * @return result of getResult
     */
    Result getResult(Integer number);
}
