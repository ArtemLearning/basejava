package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            throw (new RuntimeException("База резюме полностью заполнена"));
        } else if (getIndex(r.getUuid()) > 0) {
            throw (new RuntimeException("Резюме c uuid " + r.getUuid() + " уже есть в базе"));
        } else {
            storage[size] = r;
            size++;
        }
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
    public final void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw (new RuntimeException("В БД нет резюме c uuid " + r.getUuid()));
        } else {
            storage[index] = r;
        }
    }
}
