package ru.timestop.entrance.service.db;

import ru.timestop.entrance.entities.ResultEntity;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 29.09.2018
 */
//Опять же название тут бы подошло SearchResultStorage
public interface ResultEntityService {

    // вижу ты не пользуешься этой функцией, это уже мёртвый код, но к тому же она нарушает абстракцию этого сервиса
    // необходимого исключительно для операций сохранения и получения результатов поиска в БД
    ResultEntity createNew(Integer number);

    void registration(ResultEntity result);

    ResultEntity getRegistredResult(Integer number);
}