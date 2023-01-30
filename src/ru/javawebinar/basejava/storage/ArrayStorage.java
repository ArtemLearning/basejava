package ru.javawebinar.basejava.storage;

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
    protected void saveArrayElement(Resume r) {
        storage[size] = r;
    }

    @Override
    protected void deleteArrayElement(Object searchKey) {
        int index = (Integer) searchKey;
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
    }
}
