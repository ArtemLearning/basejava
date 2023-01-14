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
    private static final String UUID_NOT_EXIST = "dummy";
    private static final String ERROR_MESSAGE = "Ошибка сравнения";
    private static final Resume RESUME_1 = new Resume(UUID_1);
    private static final Resume RESUME_2 = new Resume(UUID_2);
    private static final Resume RESUME_3 = new Resume(UUID_3);
    private static final Resume[] expectedStorage = new Resume[]{RESUME_1, RESUME_2, RESUME_3};
    private static final Resume REF_RESUME = new Resume("refUuid");
    private final Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws StorageException {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void get() throws NotExistStorageException {
        assertGet(storage.get(UUID_1));
        assertGet(storage.get(UUID_2));
        assertGet(storage.get(UUID_3));
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        Assert.assertArrayEquals(ERROR_MESSAGE, expectedStorage, storage.getAll());
    }

    @Test
    public void getAll() {
        assertSize(expectedStorage.length);
        Assert.assertArrayEquals(ERROR_MESSAGE, expectedStorage, storage.getAll());
    }

    @Test
    public void update() throws NotExistStorageException {
        storage.update(RESUME_1);
        Assert.assertSame(RESUME_1, storage.get(RESUME_1.getUuid()));
        storage.update(RESUME_2);
        Assert.assertSame(RESUME_2, storage.get(RESUME_2.getUuid()));
        storage.update(RESUME_3);
        Assert.assertSame(RESUME_3, storage.get(RESUME_3.getUuid()));

    }

    @Test
    public void save() throws StorageException {
        storage.save(REF_RESUME);
        assertGet(REF_RESUME);
        assertSize(4);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws NotExistStorageException {
        storage.delete(UUID_2);
        assertSize(storage.size());
        storage.get(UUID_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID_NOT_EXIST);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume(UUID_NOT_EXIST));
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
        storage.save(new Resume(UUID_NOT_EXIST));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NOT_EXIST);
    }

    private void assertSize(int expected) {
        Assert.assertEquals(ERROR_MESSAGE, expected, storage.size());
    }

    private void assertGet(Resume r) {
        Assert.assertEquals(ERROR_MESSAGE, r, storage.get(r.getUuid()));
    }
}