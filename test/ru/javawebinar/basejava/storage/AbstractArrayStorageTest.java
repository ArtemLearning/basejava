package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private final Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws StorageException {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void get() throws NotExistStorageException {
        storage.get(UUID_1);
        storage.get(UUID_2);
        storage.get(UUID_3);
    }

    @Test
    public void clear() {
        storage.clear();
    }

    @Test
    public void getAll() {
        storage.getAll();
    }

    @Test
    public void update() throws NotExistStorageException {
        storage.update(new Resume(UUID_1));
        storage.update(new Resume(UUID_2));
        storage.update(new Resume(UUID_3));
    }

    @Test
    public void save() throws StorageException {
        storage.save(new Resume("dummy"));
    }

    @Test
    public void delete() throws NotExistStorageException {
        storage.delete(UUID_1);
        storage.delete(UUID_2);
        storage.delete(UUID_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume("dummy"));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                String uuid = "uuid" + i;
                storage.save(new Resume(uuid));
            }
        } catch (RuntimeException e) {
            Assert.fail("Раннее переполнение");
        }
        storage.save(new Resume("dummy"));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }
}