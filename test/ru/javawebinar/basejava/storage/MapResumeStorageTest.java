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
        assertEquals(R1, storage.get(UUID_1));
        assertEquals(R2, storage.get(UUID_2));
        assertEquals(R3, storage.get(UUID_3));

    }

}