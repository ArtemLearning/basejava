package ru.javawebinar.basejava.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    protected static final File PROPS = new File(".\\config\\resumes.properties");
    private static final Config INSTANCE = new Config();
    Properties props = new Properties();
    private final File storageDir;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try {
            InputStream is = new FileInputStream(PROPS);
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());

        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public String getUrl() {
        return props.getProperty("db.url");
    }

    public String getUser() {
        return props.getProperty("db.user");
    }

    public String getPassword() {
        return props.getProperty("db.password");
    }
}
