package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public final int size() {
        return size;
    }

    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            throw (new RuntimeException("В БД нет резюме c uuid " + uuid));
        }
    }

    public final void clear() {
        if (size > 0) {
            Arrays.fill(storage, 0, size, null);
            size = 0;
        }
    }

    public final Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public abstract void update(Resume r);

    public abstract void save(Resume r);

    public abstract void delete(String uuid);

    protected abstract int getIndex(String uuid);
}
