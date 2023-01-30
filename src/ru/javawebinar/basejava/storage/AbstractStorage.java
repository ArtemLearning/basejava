package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public final Resume get(String uuid) {
        Object searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    public final void save(Resume r) {
        Object searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(r);
    }

    public final void update(Resume r) {
        Object searchKey = getExistingSearchKey(r.getUuid());
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

    protected abstract Resume[] getStorage();

    public abstract Resume[] getAll();

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(String uuid);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doSave(Resume r);

    protected abstract void doUpdate(Object searchKey, Resume r);

    protected abstract void doDelete(Object searchKey);
}
