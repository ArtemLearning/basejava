package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.strategy.ObjectStreamStorage;

public class PathStorageTest extends AbstractStorageTest {
    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStreamStorage()));
    }
}