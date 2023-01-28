package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void doSave(Resume r) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("База резюме полностью заполнена", r.getUuid());
        }
        storage[size] = r;
        size++;
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        int index = (Integer) searchKey;
        storage[index] = r;
    }

    @Override
    protected void doDelete(Object searchKey) {
        int index = (Integer) searchKey;
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
        size--;
    }
}