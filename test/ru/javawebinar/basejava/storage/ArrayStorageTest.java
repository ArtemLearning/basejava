package ru.javawebinar.basejava.storage;

public class ArrayStorageTest extends AbstractArrayStorageTest {
    private static final Storage storage = new ArrayStorage();

    public ArrayStorageTest() {
        super(storage);
    }
}