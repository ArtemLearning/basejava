package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractArrayStorage {
    protected ArrayList<Resume> storage;

    @Override
    protected void insertElement(Resume r, int index) {
        storage.add(r);
    }

    @Override
    protected void fillDeletedElement(int index) {
        storage.remove(index);
    }

    @Override
    protected int getIndex(String uuid) {
        if (storage.isEmpty()) {
            return -1;
        }
        Resume r = new Resume(uuid);
        return storage.indexOf(r);
    }

    @Override
    protected Resume getElement(int index) {
        return storage.get(index);
    }

    @Override
    protected Resume[] getStorage() {
        storage = new ArrayList<>();
        return storage.toArray(new Resume[0]);
    }

    @Override
    protected void updateStorage(Resume r, int index) {
        storage.set(index, r);
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    protected void clearStorage() {
        storage.clear();
    }
}
