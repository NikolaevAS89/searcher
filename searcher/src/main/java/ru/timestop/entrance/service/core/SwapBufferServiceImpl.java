package ru.timestop.entrance.service.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.timestop.entrance.entities.SearchResult;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 10.10.2018
 */
@Component
public class SwapBufferServiceImpl implements SwapBufferService {

    private static final Logger LOG = Logger.getLogger(SwapBufferServiceImpl.class);

    @Value("${searcher.root.directory:data}")
    private String rootDirectory;

    @Value("${searcher.file.pattern:d[0-9]+}")
    private String pattern;

    private Map<String, File> filesMap;

    private Map<String, Map<Integer, SearchResult>> swapsMap;

    @PostConstruct
    public void init() {
        Map<String, File> mapFiles = new HashMap<>();
        Map<String, Map<Integer, SearchResult>> mapSwaps = new HashMap<>();

        File rootDir = new File(rootDirectory);
        File[] files = rootDir.listFiles((dir, name) -> name.matches(pattern));
        for (File file : files) {
            String fileId = file.getName();
            mapFiles.put(fileId, file);
            mapSwaps.put(fileId, new ConcurrentSkipListMap<>());
            LOG.info("Add " + fileId + " for numbers search");

        }
        this.filesMap = Collections.unmodifiableMap(mapFiles);
        this.swapsMap = Collections.unmodifiableMap(mapSwaps);
    }

    @Override
    public Set<String> getFileIds() {
        return filesMap.keySet();
    }

    @Override
    public File getFile(String fileId) {
        return filesMap.get(fileId);
    }

    @Override
    public void numberFound(String fileId, Integer number) {
        Map<Integer, SearchResult> numbersMap = swapsMap.get(fileId);
        SearchResult result = numbersMap.get(number);
        if (result != null) {
            result.setFound();
        }
    }

    @Override
    public void numberNotFound(String fileId, Integer number) {
        Map<Integer, SearchResult> resultMap = swapsMap.get(fileId);
        SearchResult result = resultMap.get(number);
        if (result != null) {
            result.setNotFound();
        }
    }

    @Override
    public void put(String fileId, Integer number) {
        swapsMap.get(fileId).putIfAbsent(number, new SearchResult());
    }

    @Override
    public SearchResult remove(String fileId, Integer number) {
        return swapsMap.get(fileId).remove(number);
    }

    @Override
    public Set<Integer> getTasksCopy(String fileId) {
        return new TreeSet<>(swapsMap.get(fileId).keySet());
    }
}
