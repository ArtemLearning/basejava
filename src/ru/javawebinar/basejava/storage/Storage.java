package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

// TODO refactor getAllSorted by fullName

public interface Storage {

    void clear();

    void save(Resume r);

    void update(Resume r);

    Resume get(String uuid);

    void delete(String uuid);

    Resume[] getAll();

//    List<Resume> getAllSorted();

    int size();
}
