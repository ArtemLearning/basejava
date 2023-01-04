package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            throw (new RuntimeException("База резюме полностью заполнена"));
        } else if (getIndex(r.getUuid()) > 0) {
            throw (new RuntimeException("Резюме c uuid " + r.getUuid() + " уже есть в базе"));
        } else {
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
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            storage[size - 1] = null;
            size--;
            for (int j = index; j < size; j++) {
                storage[j] = storage[j + 1];
            }
        } else {
            throw (new RuntimeException("Нет резюме с uuid " + uuid));
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume index = new Resume();
        index.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, index);
    }

    @Override
    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw (new RuntimeException("В БД нет резюме c uuid " + r.getUuid()));
        } else {
            storage[index] = r;
        }
    }
}
