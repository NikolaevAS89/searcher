package ru.timestop.entrance.generator;

import java.io.*;
import java.util.Random;
import org.apache.log4j.Logger;
import static ru.timestop.entrance.utilites.IOUtil.closeQuiet;
import static ru.timestop.entrance.utilites.IOUtil.flushQuiet;

/**
 * generate number from range 0 and 65536
 * avarage number length is 4.8 (5)
 * 1 Gb = 1073741824 byte ~ 185127900 number
 *
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 29.09.2018
 */
public class Generator {
    private static final Logger LOG = Logger.getLogger(Generator.class);

    private static final int NUMBER_COUNT = 185127900;
    private static final int UPPER_BOUND = 65536;
    private static final String FILES_MASK = "numbers%d.data";

    private final int filesCount;

    public Generator(int filesCount) {
        this.filesCount = filesCount;
    }

    public void generate() {
        Random rand = new Random();
        OutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        for (int j = 0; j < filesCount; j++) {
            long time = System.currentTimeMillis();
            String fileName = String.format(FILES_MASK, j);
            try {
                fos = new FileOutputStream(fileName);
                osw = new OutputStreamWriter(fos);
                bw = new BufferedWriter(osw);
                for (int i = 0; i < NUMBER_COUNT; i++) {
                    bw.write(String.valueOf(Math.abs(rand.nextInt(UPPER_BOUND))));
                    bw.write(";");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                flushQuiet(bw);
                closeQuiet(bw);
                closeQuiet(osw);
                closeQuiet(fos);
            }
            time = (System.currentTimeMillis() - time) / 1000;
            System.out.println("time elapsed : " + time);
        }
    }

    public static void main(String[] args) {
        new Generator(4).generate();
    }
}
