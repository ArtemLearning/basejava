package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.StreamStrategy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final StreamStrategy strategy;

    protected PathStorage(String dir, StreamStrategy strategy) {
        Objects.requireNonNull(dir, "directory must not be null");
        directory = Paths.get(dir);
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
        this.strategy = strategy;
    }

    @Override
    public void clear() {
        getList().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return getList().toList().size();
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            strategy.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Resume r, Path path) {
        Path p;
        try {
            p = Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Cannot save path ", r.getUuid(), e);
        }
        doUpdate(r, p);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return strategy.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new StorageException("Cannot delete ", path.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getList().map(this::doGet).collect(Collectors.toList());
    }

    private Stream<Path> getList() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Cannot get file list in ", directory.toString(), e);
        }
    }
}
