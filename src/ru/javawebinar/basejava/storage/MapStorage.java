package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume[] getStorage() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public void clear() {
        if (!storage.isEmpty()) {
            storage.clear();
        }
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        String key = (String) searchKey;
        return storage.containsKey(key);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        String key = (String) searchKey;
        return storage.get(key);
    }

    @Override
    protected void doSave(Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        storage.put(searchKey.toString(), r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        String key = (String) searchKey;
        storage.remove(key);
    }

    public int size() {
        return storage.size();
    }
}
