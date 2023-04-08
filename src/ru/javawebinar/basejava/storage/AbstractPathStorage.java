package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> implements SerializationStrategy {
    private final Path directory;
    SerializationStrategy strategy;

//    protected abstract void doWrite(Resume r, OutputStream os) throws IOException;
//
//    protected abstract Resume doRead(InputStream is) throws IOException;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        try {
            return Files.list(directory).toList().size();
        } catch (IOException e) {
            throw new StorageException("Cannot calculate size in ", directory.toString());
        }

    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory.toString(), uuid);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid());
        }
    }

    @Override
    protected boolean isExist(Path path) {
        if (path == null) {
            return false;
        } else {
            return Files.exists(path);
        }
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            doUpdate(r, Files.createFile(path));
        } catch (IOException e) {
            throw new StorageException("Cannot save path ", r.getUuid(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getFileName().toString());
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new StorageException("Cannot delete ", path.getFileName().toString());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        try {
            Files.list(directory).forEach(path -> {
                resumes.add(doGet(path));
            });
        } catch (IOException e) {
            throw new StorageException("Cannot copy ", directory.getFileName().toString());
        }
        return resumes;
    }
}
