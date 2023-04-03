package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.basejava.ResumeTestData.fillTestResume;

public abstract class AbstractStorageTest {
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String FULL_NAME1 = "Bracer Phoenix";
    protected static final String FULL_NAME2 = "Coyote Tango";
    protected static final String FULL_NAME3 = "Striker Eureka";
    protected static final String UUID_NOT_EXIST = "dummy";
    protected static final String SPACE = "";
    protected static final Resume RESUME_1;
    protected static final Resume RESUME_2;
    protected static final Resume RESUME_3;
    protected static final Resume[] expectedStorage;
    protected static final Resume REF_RESUME;

    static {
        RESUME_1 = fillTestResume(UUID_1, FULL_NAME1);
        RESUME_2 = fillTestResume(UUID_2, FULL_NAME2);
        RESUME_3 = fillTestResume(UUID_3, FULL_NAME3);
        expectedStorage = new Resume[]{RESUME_1, RESUME_2, RESUME_3};
        REF_RESUME = new Resume("refUuid");
    }

    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
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
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test
    public void clear() {
        assertArrayEquals(expectedStorage, storage.getAllSorted().toArray());
        storage.clear();
        assertSize(0);
    }

    @Test
    public void getAllSorted() {
        List<Resume> array = storage.getAllSorted();
        assertEquals(3, array.size());
        assertEquals(RESUME_1, array.get(0));
        assertEquals(RESUME_2, array.get(1));
        assertEquals(RESUME_3, array.get(2));
    }

    @Test
    public void update() throws NotExistStorageException {
        Resume newResume = new Resume(UUID_1, FULL_NAME1);
        storage.update(newResume);
        assertSame(newResume, storage.get(UUID_1));
    }

    @Test
    public void save() throws StorageException {
        storage.save(REF_RESUME);
        assertSize(4);
        assertGet(REF_RESUME);
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
        storage.update(new Resume(UUID_NOT_EXIST, SPACE));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(new Resume(UUID_1, FULL_NAME1));
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID_NOT_EXIST);
    }

    protected void assertSize(int expected) {
        assertEquals(expected, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}