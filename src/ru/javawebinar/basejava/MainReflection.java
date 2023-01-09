package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.Field;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException {
        Resume r = new Resume();
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        field.get(r);
        field.set(r, "new");
        System.out.println("toString direct from Resume " + r.toString());
        System.out.println("toString via reflection " + field.get(r).toString());
        System.out.println(r);
    }
}
