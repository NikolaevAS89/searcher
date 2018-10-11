package ru.timestop.entrance.service.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.timestop.entrance.entities.AgregatedSearchResult;
import ru.timestop.entrance.entities.ResultCode;
import ru.timestop.entrance.entities.ResultEntity;
import ru.timestop.entrance.exception.NumberNotFoundException;
import ru.timestop.entrance.generated.ObjectFactory;
import ru.timestop.entrance.generated.Result;
import ru.timestop.entrance.service.core.FileSearchServiceImpl;
import ru.timestop.entrance.service.db.ResultEntityService;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 11.10.2018
 */
@Component
public class ResultServiceImpl implements ResultService {
    private static final Logger LOG = Logger.getLogger(ResultServiceImpl.class);

    @Autowired
    private FileSearchServiceImpl searchService;

    @Autowired
    private ResultEntityService resultEntityService;

    private final ObjectFactory objectFactory;

    public ResultServiceImpl() {
        objectFactory = new ObjectFactory();
    }

    @Override
    public Result getResult(Integer number) {
        ResultEntity resultEntity = null;
        try {
            resultEntity = resultEntityService.getRegistredResult(number);
        } catch (DataAccessException e) {
            LOG.warn(e);
        }
        if (resultEntity == null) {
            ResultEntity.Builder builder = new ResultEntity.Builder(number);
            try {
                Future<AgregatedSearchResult> future = searchService.search(number);
                AgregatedSearchResult agregated = future.get();

                if (agregated.getFiles().isEmpty()) {
                    builder.setCode(ResultCode.NOT_FOUND).setError(new NumberNotFoundException());
                } else {
                    for (String fileId : agregated.getFiles()) {
                        builder.addFilename(fileId);
                    }
                }
                if (!agregated.getExceptions().isEmpty()) {
                    for (Throwable e : agregated.getExceptions()) {
                        builder.setError(e);
                    }
                    builder.setCode(ResultCode.ERROR);
                }
            } catch (Throwable e) {
                builder.setCode(ResultCode.ERROR).setError(e);
            }
            resultEntity = builder.build();
        }
        try {
            resultEntityService.registration(resultEntity);
        } catch (DataAccessException e) {
            LOG.error(e);
        }
        return toResult(resultEntity);
    }

    /**
     * @param entity
     * @return
     */
    private Result toResult(ResultEntity entity) {
        if (entity == null) {
            return null;
        }
        Result result = objectFactory.createResult();
        result.setCode(entity.getCode());
        result.setError(entity.getError());
        List<String> files = result.getFileNames();
        files.addAll(entity.getFiles());
        return result;
    }
}