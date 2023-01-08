package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SortedArrayStorage;

/*
 Test for your com.urise.webapp.storage.ArrayStorage implementation
*/
public class MainTestArrayStorage {
    private static final SortedArrayStorage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        final Resume r1 = new Resume("uuid1");
        final Resume r2 = new Resume("uuid2");
        final Resume r3 = new Resume("uuid3");

        //save
        try {
            ARRAY_STORAGE.save(r1);
            ARRAY_STORAGE.save(r2);
            ARRAY_STORAGE.save(r3);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        //get
        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));

        //size
        System.out.println("Size: " + ARRAY_STORAGE.size());

        //update
        try {
            ARRAY_STORAGE.update(r1);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        //dummy should return error
        try {
            System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        printAll();

        //delete
        try {
            ARRAY_STORAGE.delete(r1.getUuid());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        printAll();

        //clear
        ARRAY_STORAGE.clear();

        printAll();

        //size after clear
        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}