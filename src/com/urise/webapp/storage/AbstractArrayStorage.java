package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int MAX_SIZE = 10000;
    protected final Resume[] storage = new Resume[MAX_SIZE];
    protected int size;

    public int size() {
        return size;
    }

    public Resume get(String uuid) {
        int index = getSearchKey(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            throw (new RuntimeException("В БД нет резюме c uuid " + uuid));
        }
    }

    public void update(Resume r) {
        int index = getSearchKey(r.getUuid());
        if (index < 0) {
            throw (new RuntimeException("В БД нет резюме c uuid " + r.getUuid()));
        } else {
            storage[index] = r;
        }
    }

    public void clear() {
        if (size > 0) {
            Arrays.fill(storage, 0, size, null);
            size = 0;
        }
    }

    public void save(Resume r) {
        if (size == MAX_SIZE) {
            throw (new RuntimeException("База резюме полностью заполнена"));
        } else if (getSearchKey(r.getUuid()) > 0) {
            throw (new RuntimeException("Резюме c uuid " + r.getUuid() + " уже есть в базе"));
        } else {
            storage[size] = r;
            size++;
        }
    }

    public void delete(String uuid) {
        int index = getSearchKey(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            throw (new RuntimeException("Нет резюме с uuid " + uuid));
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected abstract int getSearchKey(String uuid);
}
