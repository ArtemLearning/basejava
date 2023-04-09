package ru.javawebinar.basejava.storage;

public class AbstractPathStorageTest extends AbstractStorageTest {
    public AbstractPathStorageTest() {
        super(new AbstractPathStorage(STORAGE_DIR, new ObjectStreamStorage()));
    }
}