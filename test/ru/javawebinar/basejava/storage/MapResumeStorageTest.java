package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;

public class MapResumeStorageTest extends AbstractStorageTest {

    private static final Storage storage = new MapResumeStorage();

    public MapResumeStorageTest() {
        super(storage);
    }

    public void saveOverflow() {
        throw new StorageException("Проверка MapStorageTest", UUID_NOT_EXIST);
    }
}