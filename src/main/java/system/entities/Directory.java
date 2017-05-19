package system.entities;

import system.exceptions.IllegalOperationException;
import system.exceptions.NoSuchDirectoryException;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static system.utils.Utils.PARENT_DIR;

/**
 * Created by Dan on 3/20/2017.
 */
public class Directory extends FileSystemEntity {

    // Predicates
    public static Predicate<FileSystemEntity> isDirectory = fileSystemEntity -> fileSystemEntity.getClass().equals(Directory.class);
    private static Predicate<FileSystemEntity> isFile = fileSystemEntity -> !isDirectory.test(fileSystemEntity);

    // Fields
    private List<FileSystemEntity> content;

    // Constructors
    public Directory(String name) {
        this(name, null);
    }

    public Directory(String name, Directory parent) {
        this(name, parent, null);
    }

    public Directory(String name, Directory parent, java.io.File concreteFile) {
        super(name, parent, concreteFile);
        this.content = new LinkedList<>();
    }

    // Methods
    public void addFileSystemEntity(FileSystemEntity fileSystemEntity) {
        fileSystemEntity.setParent(this);
        content.add(fileSystemEntity);
    }

    public void removeFileEntity(FileSystemEntity fileSystemEntity) throws IllegalOperationException {
        if (!content.remove(fileSystemEntity)) {
            throw new IllegalOperationException("Error: Failed to remove file: " + fileSystemEntity.getName() +
                                                " not exist in " + getName());
        }
    }

    // Find Methods

    private Supplier<Stream<Directory>> findDirectorySupplier(String name) {
        return () -> content.stream()
                .filter(dir -> dir.getName().equals(name))
                .filter(isDirectory)
                .map(dir -> (Directory)dir);
    }

    private Supplier<Stream<File>> findFileSupplier(String name) {
        return () -> content.stream()
                .filter(file -> file.getName().equals(name))
                .filter(isFile)
                .map(file -> (File)file);
    }

    public Directory getInnerDirectory(String name) throws NoSuchDirectoryException {
        if (name.equals(PARENT_DIR)) {
            return getParent() != null ? getParent() : this;
        }

        return findDirectorySupplier(name).get()
                .findFirst()
                .orElseThrow(NoSuchDirectoryException::new);
    }

    public Directory findDirectory(String name) {
        return findDirectorySupplier(name).get()
                .findFirst()
                .orElse(null);
    }

    public File findFile(String name) {
        return findFileSupplier(name).get()
                .findFirst()
                .orElseThrow(NoSuchDirectoryException::new);
    }

    public FileSystemEntity findFileSystemEntity(String name) {
        return content.stream()
                .filter(fileSystemEntity -> fileSystemEntity.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchDirectoryException::new);
    }

    // Override Methods

    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    public void deleteContent() throws IllegalOperationException {
        for (FileSystemEntity fileSystemEntity : content) {
            fileSystemEntity.delete();
        }
    }

    @Override
    public String toString() {
        StringJoiner sb = new StringJoiner(" ");
        content.forEach(fileSystemEntity -> sb.add(fileSystemEntity.getName()));
        return sb.toString();
    }
}
