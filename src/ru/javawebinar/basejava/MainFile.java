package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    public static void getAllFiles(File path) {
        if (!path.isDirectory()) {
            System.out.println("Not a directory");
            return;
        }
        File[] list = path.listFiles();
        if (list == null) {
            return;
        }
        for (File file : list) {
            if (file.isDirectory()) {
                getAllFiles(file);
                System.out.println("Folder: " + file.getPath());
            } else {
                System.out.println("File: " + file.getPath());
            }
        }
    }

    public static void main(String[] args) {
//        String filePath = ".\\.gitignore";
//
//        File file = new File(filePath);
//        try {
//            System.out.println(file.getCanonicalPath());
//        } catch (IOException e) {
//            throw new RuntimeException("Error", e);
//        }
        File dir = new File(".\\src\\ru\\javawebinar\\basejava\\model");
//        System.out.println(dir.getAbsolutePath());
//        String[] list = dir.list();
//        if (list != null) {
//            for (String name : list) {
//                System.out.println(name);
//            }
//        }
//        try (FileInputStream fis = new FileInputStream(filePath)) {
//            System.out.println(fis.read());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        getAllFiles(dir);
    }
}
