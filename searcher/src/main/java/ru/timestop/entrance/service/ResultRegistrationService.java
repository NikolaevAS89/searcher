package ru.timestop.entrance.service;

import ru.timestop.entrance.entities.RegistredResult;
import ru.timestop.entrance.generated.Result;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 29.09.2018
 */
public interface ResultRegistrationService {

    RegistredResult registration(Result result);

    RegistredResult registration(Result result, Exception e);

    RegistredResult getRegistredResult(Integer number);
}