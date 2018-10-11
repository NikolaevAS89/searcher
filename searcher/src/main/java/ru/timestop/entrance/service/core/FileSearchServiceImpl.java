package ru.timestop.entrance.service.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.timestop.entrance.entities.AgregatedSearchResult;
import ru.timestop.entrance.entities.SearchResult;
import ru.timestop.entrance.utilites.IOUtil;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 11.10.2018
 */
@Component
public class FileSearchServiceImpl implements FileSearchService {

    private static final Logger LOG = Logger.getLogger(FileSearchServiceImpl.class);

    @Value("${searcher.aggregator.thread.count:4}")
    private int aggregatorsThreadCount;

    @Value("${searcher.searcher.thread.count:16}")
    private int searcherThreadCount;

    @Autowired
    private SwapBufferService swapBufferService;

    private final Map<Integer, Future<AgregatedSearchResult>> results;

    private ExecutorService aggregateExecutors;
    private ExecutorService searchExecutors;

    public FileSearchServiceImpl() {
        results = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init() {
        aggregateExecutors = Executors.newFixedThreadPool(aggregatorsThreadCount);
        LOG.info(aggregatorsThreadCount + " aggregating executors was initialize");
        searchExecutors = Executors.newFixedThreadPool(searcherThreadCount);
        LOG.info(searcherThreadCount + " searching executors was initialize ");

    }

    @Override
    public Future<AgregatedSearchResult> search(Integer number) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Request for search " + number);
        }
        return results.computeIfAbsent(number, itm -> aggregateExecutors.submit(new SearchTask(itm)));
    }

    /**
     * Task for aggregate result of computing many of {@link SearchSubtask}
     * in one result object {@link AgregatedSearchResult}.
     */
    private class SearchTask implements Callable<AgregatedSearchResult> {
        private static final String THREAD_NAME = "SearchTask";

        private final Integer serchedNumber;

        SearchTask(Integer serchedNumber) {
            this.serchedNumber = serchedNumber;
        }

        @Override
        public AgregatedSearchResult call() {
            String taskName = THREAD_NAME + "[" + serchedNumber + "]";
            Thread.currentThread().setName(taskName);
            if (LOG.isDebugEnabled()) {
                LOG.debug("New Search task start " + taskName);
            }
            AgregatedSearchResult result = new AgregatedSearchResult();
            Map<String, Future<Boolean>> subtasks = new HashMap<>();
            for (String fileId : swapBufferService.getFileIds()) {
                SearchSubtask subtask = new SearchSubtask(serchedNumber, fileId);
                swapBufferService.put(fileId, serchedNumber);
                Future<Boolean> future = searchExecutors.submit(subtask);
                subtasks.put(fileId, future);
            }

            for (String fileId : subtasks.keySet()) {
                try {
                    if (subtasks.get(fileId).get()) {
                        result.addFile(fileId);
                    }
                } catch (CancellationException | InterruptedException | ExecutionException e) {
                    LOG.warn(e);
                    result.addException(e);
                }
            }
            results.remove(serchedNumber);
            return result;
        }
    }

    /**
     * Task of search numbers in one file.
     * Method {@link SearchSubtask#call()} return true if number found, else return false.
     * Exception may be throws while computing
     */
    private class SearchSubtask implements Callable<Boolean> {
        private static final String THREAD_NAME = "SearchSubTask";
        private static final int CACHE_SIZE = 1 << 16;
        private static final char SEPARATOR = ';';
        private static final int MAX_NUMBER_LENGTH = 10;

        private final String fileId;
        private final int serchedNumber;

        private boolean isFound = false;
        private Set<Integer> serchedNumbers = null;

        SearchSubtask(int serchedNumber, String fileId) {
            this.fileId = fileId;
            this.serchedNumber = serchedNumber;
        }

        @Override
        public Boolean call() throws Exception {
            FileInputStream fis = null;
            try {
                String taskName = THREAD_NAME + "[" + fileId + "] : " + serchedNumber;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("New Search task start " + taskName);
                }
                Thread.currentThread().setName(taskName);

                SearchResult result = swapBufferService.remove(fileId, serchedNumber);

                if (result.isComputed()) {
                    return result.isFound();
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("New full search start in " + taskName);
                }
                serchedNumbers = swapBufferService.getTasksCopy(fileId);

                File fileIn = swapBufferService.getFile(fileId);
                int buffered = 0;
                int readed = 0;
                boolean isSkip = false;
                byte[] cache = new byte[CACHE_SIZE];
                fis = new FileInputStream(fileIn);

                while (readed >= 0 && (!serchedNumbers.isEmpty() || !isFound)) {
                    readed = fis.read(cache, buffered, cache.length - buffered);
                    if (readed < 0) {
                        if (buffered > 0) {
                            checkBytes(cache, 0, buffered);
                        }
                    } else {
                        int start = 0;
                        int end = 0;
                        for (; end < readed + buffered; end++) {
                            if (cache[end] == SEPARATOR) {
                                if (isSkip) {
                                    isSkip = false;
                                } else {
                                    checkBytes(cache, start, end);
                                }
                                start = end + 1;
                            }
                        }
                        if (cache[end - 1] == SEPARATOR) {
                            buffered = 0;
                        } else {
                            buffered = end - start;
                            if (buffered >= MAX_NUMBER_LENGTH) {
                                buffered = 0;
                                isSkip = true;
                            } else {
                                System.arraycopy(cache, start, cache, 0, buffered);
                            }
                        }
                    }
                }
                for (Integer number : serchedNumbers) {
                    swapBufferService.numberNotFound(fileId, number);
                }
                return isFound;
            } catch (IOException e) {
                LOG.error(e);
                throw e;
            } finally {
                IOUtil.closeQuiet(fis);
            }
        }

        /**
         * transform bytes to integer value and check contain it into set. Skip wrong values.
         * Also notify agregator.
         *
         * @param cache
         * @param start
         * @param end
         */
        private void checkBytes(byte[] cache, int start, int end) {
            byte[] bytes = Arrays.copyOfRange(cache, start, end);
            try {
                int value = Integer.parseInt(new String(bytes));
                if (serchedNumbers.contains(value)) {
                    swapBufferService.numberFound(fileId, value);
                    serchedNumbers.remove(value);
                }
                if (serchedNumber == value) {
                    isFound = true;
                }
            } catch (NumberFormatException e) {
                //SKIP
            }
        }
    }
}