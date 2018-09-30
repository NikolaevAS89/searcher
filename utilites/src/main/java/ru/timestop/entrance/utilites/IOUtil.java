package ru.timestop.entrance.utilites;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * @author NikolaevAS89
 * @version 1.0.0
 * @since 12.10.2017
 */
public class IOUtil {

    public static void copy(InputStream is, OutputStream out) throws IOException {
        byte[] buff = new byte[255];
        int readed = 0;
        while ((readed = is.read(buff)) > 0) {
            out.write(buff, 0, readed);
        }
        out.flush();
        out.close();
    }

    public static void closeQuiet(Closeable io) {
        try {
            io.close();
        } catch (Exception e) {
            //SKIP
        }
    }


    public static void closeQuiet(HttpURLConnection connection) {
        try {
            if (connection != null) {
                connection.disconnect();
            }
        } catch (Exception e) {
            //SKIP
        }
    }

    public static void flushQuiet(Flushable flushable) {
        try {
            flushable.flush();
        } catch (IOException e) {
            //SKIP
        }
    }
}
