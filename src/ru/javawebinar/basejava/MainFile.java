package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    private static int deepRecursion = 0;

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
                for (int i = 0; i < deepRecursion; i++) {
                    System.out.print('\t');
                }
                System.out.print("Folder: " + file.getPath() + '\n');
                deepRecursion++;
                getAllFiles(file);
                deepRecursion = 0;
            } else {
                for (int i = 0; i < deepRecursion; i++) {
                    System.out.print('\t');
                }
                System.out.print("File: " + file.getPath() + '\n');
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
        File dir = new File(".\\src\\ru\\javawebinar\\basejava");
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
