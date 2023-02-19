package ru.javawebinar.basejava.storage;

import static org.junit.Assert.assertEquals;

public class MapResumeStorageTest extends AbstractStorageTest {

    private static final Storage storage = new MapResumeStorage();

    public MapResumeStorageTest() {
        super(storage);
    }

    @Override
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Override
    public void getAllSorted() {
        assertEquals(3, storage.size());
        assertEquals(RESUME_1, storage.get(UUID_1, ""));
        assertEquals(RESUME_2, storage.get(UUID_2, ""));
        assertEquals(RESUME_3, storage.get(UUID_3, ""));

    }

}