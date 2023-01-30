package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;

public class ListStorageTest extends AbstractStorageTest {
    private static final Storage storage = new ListStorage();

    public ListStorageTest() {
        super(storage);
    }

    public void saveOverflow() {
        throw new StorageException("Проверка ListStorageTest", UUID_NOT_EXIST);
    }
}