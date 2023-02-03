package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getUuid);

    @Override
    protected Object getSearchKey(String uuid) {
        if (size == 0) {
            return -1;
        }
        Resume check = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, check, RESUME_COMPARATOR);
    }
    @Override protected void saveArrayElement(Resume r) {
//      http://codereview.stackexchange.com/questions/36221/binary-search-for-inserting-in-array#answer-36239
        int index = Arrays.binarySearch(storage, 0, size, r);
        int insertIdx = -index - 1;
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[insertIdx] = r;
    }

    @Override
    protected void deleteArrayElement(Object searchKey) {
        int index = (Integer) searchKey;
        int numMoved = size - 1 - index;
        if (numMoved > 0) {
            System.arraycopy(storage, index + 1, storage, index, numMoved);
        }
        storage[size - 1] = null;
    }
}