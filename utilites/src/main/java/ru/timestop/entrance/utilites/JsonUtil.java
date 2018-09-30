package ru.timestop.entrance.utilites;

import com.google.gson.Gson;

import java.io.Reader;

/**
 * @author NikolaevAS89
 * @version 1.0.0
 * @since 29.06.2017
 */
public class JsonUtil {

    private static final Gson gson = new Gson();

    private JsonUtil() {
    }

    public static <T> T fromJson(String str, Class<T> classOfT) {
        return gson.fromJson(str, classOfT);
    }

    public static <T> T fromJson(Reader reader, Class<T> classOfT) {
        return gson.fromJson(reader, classOfT);
    }

    public static String toJson(Object value) {
        return gson.toJson(value);
    }
}