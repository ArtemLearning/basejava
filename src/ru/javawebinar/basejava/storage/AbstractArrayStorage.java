package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = getStorage();
    protected int size;

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    public final void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    protected Resume[] getStorage() {
        return new Resume[STORAGE_LIMIT];
    }

    @Override
    protected boolean isExist(String uuid) {
        Object searchKey = getSearchKey(uuid);
        return Integer.parseInt(String.valueOf(searchKey)) >= 0;
    }

    protected Resume doGet(Object searchKey) {
        int index = (Integer) searchKey;
        return storage[index];
    }

    protected void doSave(Resume r) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("База резюме полностью заполнена", r.getUuid());
        }
        saveArrayElement(r);
        size++;
    }

    protected void doUpdate(Object searchKey, Resume r) {
        int index = (Integer) searchKey;
        storage[index] = r;
    }

    protected void doDelete(Object searchKey) {
        deleteArrayElement(searchKey);
        size--;
    }

    protected abstract void saveArrayElement(Resume r);

    protected abstract void deleteArrayElement(Object searchKey);

}
