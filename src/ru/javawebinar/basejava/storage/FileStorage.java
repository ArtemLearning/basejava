package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.StreamStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final StreamStrategy strategy;

    protected FileStorage(String dir, StreamStrategy strategy) {
        Objects.requireNonNull(dir, "directory must not be null");
        directory = new File(dir);
        this.strategy = strategy;
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
    }

    @Override
    public void clear() {
        for (File name : getList()) {
            doDelete(name);
        }
    }

    @Override
    public int size() {
        return getList().length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            strategy.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return strategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Cannot delete directory", directory.getAbsolutePath());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        for (File name : getList()) {
            resumes.add(doGet(name));
        }
        return resumes;
    }

    private File[] getList() {
        File[] list = directory.listFiles();
        if (list == null) {
            throw new StorageException("Filesystem error", directory.getName());
        }
        return list;
    }
}




