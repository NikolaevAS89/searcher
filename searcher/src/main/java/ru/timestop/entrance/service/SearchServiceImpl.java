package ru.timestop.entrance.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.timestop.entrance.generated.Result;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 01.10.2018
 */
@Component
public class SearchServiceImpl implements SearchService {

    @Value("${data.file.directory:data}")
    private String directory;

    @Value("${searcher.thread.count:2}")
    private int threadCount;

    private ExecutorService executorService;

    @PostConstruct
    public void init(){
        executorService = Executors.newFixedThreadPool(threadCount, null);
    }

    @Override
    public Result search(Integer number) {
        return null;
    }
}