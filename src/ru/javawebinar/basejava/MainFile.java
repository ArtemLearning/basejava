package ru.javawebinar.basejava;

import java.io.File;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\TEMP\\123.txt");
        System.out.println(file.getCanonicalPath());
    }
}
