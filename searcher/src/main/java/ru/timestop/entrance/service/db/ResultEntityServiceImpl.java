package ru.timestop.entrance.service.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.timestop.entrance.entities.ResultEntity;
import ru.timestop.entrance.utilites.JsonUtil;

import java.util.Collections;
import java.util.Map;

import static ru.timestop.entrance.entities.ResultCode.ERROR;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 29.09.2018
 */
@Component
public class ResultEntityServiceImpl implements ResultEntityService {
    private static final Logger LOG = Logger.getLogger(ResultEntityServiceImpl.class);

    private static final BeanPropertyRowMapper<ResultEntity> MAPPER = new BeanPropertyRowMapper<>(ResultEntity.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private BasicDataSource dataSource;


    @Override
    public ResultEntity createNew(Integer number) {
        return new ResultEntity.Builder(number).build();
    }

    @Override
    public void registration(ResultEntity result) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("registration(" + JsonUtil.toJson(result) + ")");
            }
            SqlParameterSource source = new BeanPropertySqlParameterSource(result);

            KeyHolder keyHolder = new SimpleJdbcInsert(dataSource)
                    .withTableName("entrances")
                    .usingGeneratedKeyColumns("id")
                    .executeAndReturnKeyHolder(source);

            Number key = keyHolder.getKey();
            if (key != null) {
                result.setId(key.intValue());
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Registred " + JsonUtil.toJson(result) + " is registred");
            }
        } catch (DataAccessException e) {
            LOG.error(JsonUtil.toJson(result) + " is not registred", e);
        }
    }

    @Override
    public ResultEntity getRegistredResult(Integer number) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getRegistredResult(" + JsonUtil.toJson(number) + ")");
        }
        String sql = "select * from entrances where \"NUMBER\"=:number_value and \"CODE\"!='" + ERROR.toString() + "' limit 1";

        Map<String, Integer> params = Collections.singletonMap("number_value", number);

        try {
            return jdbcTemplate.queryForObject(sql, params, MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}