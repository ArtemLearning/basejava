package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.List;

// TODO refactor getAllSorted by fullName

public interface Storage {

    void clear();

    void save(Resume r);

    void update(Resume r);

    Resume get(String uuid, String fullName);

    void delete(String uuid, String fullName);

    List<Resume> getAllSorted();

    int size();
}
