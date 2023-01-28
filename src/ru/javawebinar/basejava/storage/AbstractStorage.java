package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected Resume[] storage = getStorage();
    protected Object searchKey;
    protected int size;

    public final int size() {
        return size;
    }

    public final Resume get(String uuid) {
        searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    public final void save(Resume r) {
        searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(r);
    }

    public final void update(Resume r) {
        searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(searchKey, r);
    }

    public final void delete(String uuid) {
        Object searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    protected Object getExistingSearchKey(String uuid) {
        if (isExist(uuid)) {
            return getSearchKey(uuid);
        }
        throw new NotExistStorageException(uuid);
    }

    protected Object getNotExistingSearchKey(String uuid) {
        if (isExist(uuid)) {
            throw new ExistStorageException(uuid);
        }
        return null;
    }

    public final void clear() {
        clearStorage();
    }

    protected abstract Resume[] getStorage();

    public abstract Resume[] getAll();

    protected abstract void clearStorage();

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(String uuid);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doSave(Resume r);

    protected abstract void doUpdate(Object searchKey, Resume r);

    protected abstract void doDelete(Object searchKey);
}
