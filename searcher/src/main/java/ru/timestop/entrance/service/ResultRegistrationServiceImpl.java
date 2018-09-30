package ru.timestop.entrance.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.timestop.entrance.converter.ResultConverter;
import ru.timestop.entrance.entities.RegistredResult;
import ru.timestop.entrance.generated.Result;
import ru.timestop.entrance.utilites.JsonUtil;

import java.util.Collections;
import java.util.Map;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 29.09.2018
 */
@Component
public class ResultRegistrationServiceImpl implements ResultRegistrationService {
    private static final Logger LOG = Logger.getLogger(ResultRegistrationServiceImpl.class);

    private static final BeanPropertyRowMapper<RegistredResult> MAPPER = new BeanPropertyRowMapper<>(RegistredResult.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BasicDataSource dataSource;

    @Autowired
    private ResultConverter resultConverter;

    @Override
    public RegistredResult registration(Result result) {
        return registration(result, null);
    }

    @Override
    public RegistredResult registration(Result result, Exception e) {

        RegistredResult registredResult = resultConverter.toRegistredResult(result);

        SqlParameterSource source = new BeanPropertySqlParameterSource(registredResult);

        KeyHolder keyHolder = new SimpleJdbcInsert(dataSource)
                .withTableName("entrances")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(source);

        Integer key = keyHolder.getKey().intValue();
        registredResult.setId(key);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Registred " + JsonUtil.toJson(registredResult) + " is created");
        }
        return registredResult;
    }

    @Override
    public RegistredResult getRegistredResult(Integer number) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("getRouteTiming(" + JsonUtil.toJson(number) + ")");
        }
        String sql = "select * from entrances where \"number\"=:value1 and \"code\"='00.Result.OK' and rownum<2";

        Map<String, Integer> params = Collections.singletonMap("value1", number);

        return jdbcTemplate.queryForObject(sql, params, MAPPER);
    }
}