package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    protected static Map<String, Resume> storage;

    @Override
    protected Resume[] getStorage() {
        storage = new HashMap<>();
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    protected void clearStorage() {
        storage.clear();
    }

    @Override
    protected Object getSearchKey(String uuid) {
        if (storage.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (uuid.equals(entry.getKey())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected boolean isExist(String uuid) {
        return getSearchKey(uuid) != null;
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

}
