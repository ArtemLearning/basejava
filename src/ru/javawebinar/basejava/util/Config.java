package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Config {
    protected static final File PROPS = new File("C:\\Users\\apogosov\\IdeaProjects\\basejava\\config\\resumes.properties"); //File(".\\config\\resumes.properties");
    private static final Config INSTANCE = new Config();
    private final Storage storage;
    private final File storageDir;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try {
            InputStream is = new FileInputStream(PROPS);
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));

        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());

        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }

}
