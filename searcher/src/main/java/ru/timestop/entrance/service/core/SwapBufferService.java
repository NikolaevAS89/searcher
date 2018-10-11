package ru.timestop.entrance.service.core;

import ru.timestop.entrance.entities.SearchResult;

import java.io.File;
import java.util.Set;

/**
 * Service for communicate executed search task and define files for search
 *
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 11.10.2018
 */
public interface SwapBufferService {

    /**
     * @param fileId is real file name
     * @return File object binding with fileId
     */
    File getFile(String fileId);

    /**
     * @return all file ids (names) that belong to search area
     */
    Set<String> getFileIds();

    /**
     * mark searched number as found in file
     *
     * @param fileId where number found
     * @param number founded number
     */
    void numberFound(String fileId, Integer number);

    /**
     * mark searched number as not found in file
     *
     * @param fileId where number not found
     * @param number that was searched
     */
    void numberNotFound(String fileId, Integer number);

    /**
     * define searched number in file for search by another tasks
     *
     * @param fileId where number will be search
     * @param number that will be search
     */
    void put(String fileId, Integer number);

    /**
     * remove searched number in file for stop search by another tasks
     *
     * @param fileId
     * @param number
     * @return result witch could be obtained by another task
     */
    SearchResult remove(String fileId, Integer number);

    /**
     * get copy of searched numbers in file
     *
     * @param fileId
     * @return
     */
    Set<Integer> getTasksCopy(String fileId);
}
