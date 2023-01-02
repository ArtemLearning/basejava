package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        Arrays.sort(storage, 0, size);
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
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
