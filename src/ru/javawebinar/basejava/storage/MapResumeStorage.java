package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume[] getStorage() {
        return doCopyAll();
    }

    @Override
    public Resume[] doCopyAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Object getSearchKey(String uuid, String fullName) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        if (searchKey == null) {
            return false;
        } else {
            Resume key = (Resume) searchKey;
            return storage.containsKey(key.getUuid());
        }
    }

    @Override
    protected Resume doGet(Object searchKey) {
        Resume key = (Resume) searchKey;
        return storage.get(key.getUuid());
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        Resume key = (Resume) searchKey;
        storage.replace(key.getUuid(), r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        Resume key = (Resume) searchKey;
        storage.remove(key.getUuid());
    }
}
