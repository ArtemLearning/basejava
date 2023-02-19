package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

public abstract class AbstractStorage implements Storage {

    public final Resume get(String uuid, String fullName) {
        Object searchKey = getExistingSearchKey(uuid, fullName);
        return doGet(searchKey);
    }

    public final void save(Resume r) {
        Object searchKey = getNotExistingSearchKey(r.getUuid(), r.getFullName());
        doSave(r);
    }

    public final void update(Resume r) {
        Object searchKey = getExistingSearchKey(r.getUuid(), r.getFullName());
        doUpdate(searchKey, r);
    }

    public final void delete(String uuid, String fullName) {
        Object searchKey = getExistingSearchKey(uuid, fullName);
        doDelete(searchKey);
    }

    protected Object getExistingSearchKey(String uuid, String fullName) {
        Object searchKey = getSearchKey(uuid, fullName);
        if (isExist(searchKey)) {
            return searchKey;
        }
        throw new NotExistStorageException(uuid);
    }

    protected Object getNotExistingSearchKey(String uuid, String fullName) {
        Object searchKey = getSearchKey(uuid, fullName);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return null;
    }

    protected abstract Resume[] getStorage();

    public abstract List<Resume> getAllSorted();

    protected abstract Object getSearchKey(String uuid, String fullName);

    protected abstract boolean isExist(Object searchKey);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doSave(Resume r);

    protected abstract void doUpdate(Object searchKey, Resume r);

    protected abstract void doDelete(Object searchKey);
}
