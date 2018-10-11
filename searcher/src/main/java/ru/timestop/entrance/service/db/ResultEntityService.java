package ru.timestop.entrance.service.db;

import ru.timestop.entrance.entities.ResultEntity;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 29.09.2018
 */
public interface ResultEntityService {

    ResultEntity createNew(Integer number);

    void registration(ResultEntity result);

    ResultEntity getRegistredResult(Integer number);
}