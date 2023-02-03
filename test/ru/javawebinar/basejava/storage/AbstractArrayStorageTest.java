package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest{

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }
// TODO Remove implementation for Map & List Test
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                String uuid = "uuid" + i;
                storage.save(new Resume(uuid));
            }
        } catch (RuntimeException e) {
            fail("Раннее переполнение");
        }
        storage.save(new Resume(UUID_NOT_EXIST));
    }
}