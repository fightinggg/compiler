package com.example.visiable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DotUtils {
    public static void writeDotFile(String name, String dot) {
        try (OutputStream outputStream = new FileOutputStream(name)) {
            outputStream.write(dot.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
