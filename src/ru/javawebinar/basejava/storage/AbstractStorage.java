package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected static final int STORAGE_LIMIT = 10000;
    protected int size;
    protected Resume[] storage = getStorage();

    public final int size() {
        return size;
    }

    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            return getElement(index);
        }
    }

    public final void clear() {
        if (size > 0) {
            clearStorage();
            size = 0;
        }
    }

    public final void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            updateStorage(r, index);
        }
    }

    public final void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (size == STORAGE_LIMIT) {
            throw new StorageException("База резюме полностью заполнена", r.getUuid());
        } else if (index < 0) {
            insertElement(r, index);
            size++;
        } else {
            throw new ExistStorageException(r.getUuid());
        }

    }

    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            fillDeletedElement(index);
            size--;

        }
    }

    protected abstract void insertElement(Resume r, int index);

    protected abstract void fillDeletedElement(int index);

    protected abstract int getIndex(String uuid);

    protected abstract Resume getElement(int index);

    protected abstract Resume[] getStorage();

    protected abstract void updateStorage(Resume r, int index);

    public abstract Resume[] getAll();

    protected abstract void clearStorage();
}
