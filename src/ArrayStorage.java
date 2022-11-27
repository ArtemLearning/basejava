import java.util.Arrays;

public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
    }

    void save(Resume r) {
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            Resume resume = storage[i];
            if (resume.uuid.equals(uuid)) {
                return resume;
            }
        }
        Resume resume = new Resume();
        resume.uuid = "dummy";
        return resume;
    }

    void delete(String uuid) {
        int position = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                position = i;
            }
            if (position != -1) {
                System.arraycopy(storage, position + 1, storage, position, size - (position + 1));
                size--;
            } else {
                System.out.println("Нет резюме с таким uuid");
            }
        }
    }

    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }
}
