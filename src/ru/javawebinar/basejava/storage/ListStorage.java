package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {
    protected ArrayList<Resume> storage = new ArrayList<>();

    @Override
    public Resume[] getStorage() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public Resume[] doCopyAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public void clear() {
        if (!storage.isEmpty()) {
            storage.clear();
        }
    }

    @Override
    protected Object getSearchKey(String uuid, String fullName) {
        for (int i = 0; i < storage.size(); i++) {
            if (uuid.equals(storage.get(i).getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get((Integer) searchKey);
    }

    @Override
    protected void doSave(Object searchKey, Resume r) {
        int index = (searchKey == null) ? 0 : (Integer) searchKey;
        storage.add(index, r);
    }

    @Override
    protected void doUpdate(Object searchKey, Resume r) {
        int index = (Integer) searchKey;
        storage.set(index, r);
    }

    @Override
    protected void doDelete(Object searchKey) {
        int index = (Integer) searchKey;
        storage.remove(index);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
