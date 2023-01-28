package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = getStorage();

    @Override
    protected Resume[] getStorage() {
        return new Resume[STORAGE_LIMIT];
    }

    @Override
    protected boolean isExist(String uuid) {
        return (Integer) getSearchKey(uuid) >= 0;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        int index = (Integer) searchKey;
        return storage[index];
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected void clearStorage() {
        Arrays.fill(storage, 0, size, null);
    }
}
