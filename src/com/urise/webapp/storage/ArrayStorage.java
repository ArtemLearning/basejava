package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    private static final int MAX_SIZE = 10000;
    Resume[] storage = new Resume[MAX_SIZE];
    private int size;

    public void clear() {
        for (int i = size - 1; i >= 0; i--) {
            storage[i] = new Resume();
            size--;
        }
    }

    public void save(Resume r) {
        if (size == MAX_SIZE) {
            throw (new RuntimeException("База резюме полностью заполнена"));
        }
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(r.getUuid())) {
                throw (new RuntimeException("Резюме c uuid " + r.getUuid() + " уже есть в базе"));
            }
        }
        storage[size] = r;
        size++;
    }

    public void update(Resume r) {
        boolean check = false;
        for (int i = 0; i < size; i++) {
            if (storage[i] == r) {
                storage[i].setUuid(r.getUuid());
                check = true;
            }
        }
        if (!check) {
            throw (new RuntimeException("Нет резюме " + r));
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            Resume r = storage[i];
            if (r.getUuid().equals(uuid)) {
                return r;
            }
        }
        throw (new RuntimeException("Нет резюме с uuid " + uuid));
    }

    public void delete(String uuid) {
        boolean check = false;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
                check = true;
            }
        }
        if (!check) {
            throw (new RuntimeException("Нет резюме с uuid " + uuid));
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }
}
