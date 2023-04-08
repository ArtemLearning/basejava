package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static ru.javawebinar.basejava.ResumeTestData.fillTestResume;

public abstract class AbstractStorageTest {

    protected static final String STORAGE_DIR = "C:\\Users\\apogosov\\IdeaProjects\\basejava\\storage";
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final String UUID_NOT_EXIST = "dummy";
    protected static final String SPACE = "";
    protected static final Resume R1;
    protected static final Resume R2;
    protected static final Resume R3;
    protected static final Resume R4;
    protected static final Resume[] expectedStorage;
    protected static final Resume REF_RESUME;

    static {
        R1 = fillTestResume(UUID_1, "Name1");
        R2 = fillTestResume(UUID_2, "Name2");
        R3 = fillTestResume(UUID_3, "Name3");
        R4 = fillTestResume(UUID_4, "Name4");
        expectedStorage = new Resume[]{R1, R2, R3};
        REF_RESUME = new Resume("refUuid");

        R1.addContact(ContactType.MAIL, "mail1@ya.ru");
        R1.addContact(ContactType.PHONE, "1111111");
        R1.addSection(SectionType.OBJECTIVE, new TextSection("Objective1"));
        R1.addSection(SectionType.PERSONAL, new TextSection("Personal data"));
        R1.addSection(SectionType.ACHIEVEMENT, new TextSection("Achievement1"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("Java, SQL, JavaScript"));
        R1.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Organization11", "https://organization11.ru",
                                new Organization.Position(2005, Month.JANUARY, "position1", "content1"),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "position2", "content2"))
                ));
        R1.addSection(SectionType.EDUCATION,
                new OrganizationSection(
                        new Organization("Institute", null,
                                new Organization.Position(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT faculty")),
                        new Organization("Organization12", "https://organization12.ru")));

        R2.addContact(ContactType.SKYPE, "skype2");
        R2.addContact(ContactType.PHONE, "222222");
        R2.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Organization2", "https://organization2.ru",
                                new Organization.Position(2015, Month.JANUARY, "position2", "content2"))));
    }

    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void size() {
        assertSize(3);
    }


    @Test
    public void save() throws StorageException {
        storage.save(REF_RESUME);
        assertSize(4);
        assertGet(REF_RESUME);
    }

    @Test
    public void get() throws NotExistStorageException {
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
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
        assertEquals(R1, array.get(0));
        assertEquals(R2, array.get(1));
        assertEquals(R3, array.get(2));
    }

    @Test
    public void update() throws NotExistStorageException {
        Resume newResume = new Resume(UUID_1, "Name1");
        storage.update(newResume);
        assertEquals(newResume, storage.get(UUID_1));
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
        storage.save(new Resume(UUID_1, "Name1"));
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