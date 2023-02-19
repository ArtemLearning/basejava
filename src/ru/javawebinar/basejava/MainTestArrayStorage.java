package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.MapResumeStorage;

/*
 Test for your com.urise.webapp.storage.ArrayStorage implementation
*/
public class MainTestArrayStorage {
    public static final MapResumeStorage ARRAY_STORAGE = new MapResumeStorage();

    public static void main(String[] args) {
        final Resume r1 = new Resume("uuid1", "Striker Eureka");
        final Resume r2 = new Resume("uuid2", "Coyote Tango");
        final Resume r3 = new Resume("uuid3", "Bracer Phoenix");

        //save
        try {
            ARRAY_STORAGE.save(r1);
            ARRAY_STORAGE.save(r2);
            ARRAY_STORAGE.save(r3);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        //get
        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid(), r1.getFullName()));

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
            System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy", " "));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        printAll();

        //delete
        try {
            ARRAY_STORAGE.delete(r1.getUuid(), r1.getFullName());
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
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}