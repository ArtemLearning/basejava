package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest{

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = RuntimeException.class )
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                String fullName = "Name" + i;
                storage.save(new Resume(fullName));
            }
        } catch (RuntimeException e) {
            fail("Раннее переполнение");
        }
        storage.save(new Resume(UUID_NOT_EXIST));
    }
}