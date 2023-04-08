package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> implements SerializationStrategy {
    private final File directory;
    SerializationStrategy strategy;

    protected AbstractFileStorage(String dir) {
        directory = new File(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
    }

//    protected abstract void doWrite(Resume r, OutputStream os) throws IOException;
//
//    protected abstract Resume doRead(InputStream is) throws IOException;

    @Override
    public void clear() {
        File[] list = directory.listFiles();
        if (list != null) {
            for (File name : list) {
                doDelete(name);
            }
        } else {
            throw new StorageException("Filesystem error", directory.getName());
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Filesystem error", directory.getName());
        }
        return list.length;
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
            if (file.createNewFile()) {
                strategy.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
            }
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        Resume r;
        try {
            r = strategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
        return r;
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
        File[] list = directory.listFiles();
        if (list != null) {
            for (File name : list) {
                resumes.add(doGet(name));
            }
        } else {
            throw new StorageException("Filesystem error", directory.getName());
        }
        return resumes;
    }
}




