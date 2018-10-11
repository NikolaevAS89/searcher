package ru.timestop.entrance;

/**
 * @author t.i.m.e.s.t.o.p
 * @version 1.0.0
 * @since 30.09.2018
 */

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        try {
            FileInputStream fis = new FileInputStream("E:\\Projects\\Java\\entrance\\searcher\\data\\numbers0.data");
            for (int i = 0; i < 1000; i++) {
                System.out.print((char) fis.read());
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
