package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void saveElement(Resume r) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().compareTo(r.getUuid()) > 0) {
                for (int j = size; j > i; j--) {
                    storage[j] = storage[j - 1];
                }
                storage[i] = r;
                size++;
                return;
            }
        }
        storage[size] = r;
        size++;
    }

    @Override
    protected void deleteElement(int index) {
        for (int j = index; j < size - 1; j++) {
            storage[j] = storage[j + 1];
        }
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected int getIndex(String uuid) {
        Resume index = new Resume();
        index.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, index);
    }
}
