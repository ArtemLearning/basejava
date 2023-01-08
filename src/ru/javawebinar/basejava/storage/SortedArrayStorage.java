package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void saveElement(Resume r) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().compareTo(r.getUuid()) > 0) {
                System.arraycopy(storage, i, storage, size - 1, size - i);
                storage[i] = r;
                return;
            }
        }
        storage[size] = r;
    }

    @Override
    protected void deleteElement(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - 1 - index);
    }

    @Override
    protected int getIndex(String uuid) {
        Resume index = new Resume();
        index.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, index);
    }
}
