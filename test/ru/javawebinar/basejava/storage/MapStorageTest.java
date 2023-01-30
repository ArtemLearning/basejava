package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MapStorageTest extends AbstractStorageTest {
    private static final Storage storage = new MapStorage();

    public MapStorageTest() {
        super(storage);
    }

    public void saveOverflow() {
        throw new StorageException("Проверка MapStorageTest", UUID_NOT_EXIST);
    }

    @Override
    public void clear() {
        Resume[] array = storage.getAll();
        Arrays.sort(array);
        assertArrayEquals(expectedStorage, array);
        storage.clear();
        assertSize(0);
    }

    @Override
    public void getAll() {
        Resume[] array = storage.getAll();
        assertEquals(3, array.length);
        Arrays.sort(array);
        assertEquals(RESUME_1, array[0]);
        assertEquals(RESUME_2, array[1]);
        assertEquals(RESUME_3, array[2]);
    }
}