package system.entities;

import system.exceptions.IllegalOperationException;


/**
 * Created by Dan on 3/20/2017.
 */
public abstract class FileSystemEntity {

    // Fields
    private String name;
    private Directory parent;
    private java.io.File concreteFile;

    // Constructor
    public FileSystemEntity(String name, Directory parent, java.io.File concreteFile) {
        this.name = name;
        this.parent = parent;
        this.concreteFile = concreteFile;
    }

    // Abstract methods
    public abstract boolean isEmpty();
    public abstract void deleteContent() throws IllegalOperationException;

    // Methods

    public  String getName() {
        return name;
    }

    public void delete() throws IllegalOperationException {
        deleteContent();
        parent.removeFileEntity(this);

        if (!concreteFile.delete()) {
            throw new IllegalOperationException("Failed to delete concrete file: " + name);
        }
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public java.io.File getConcreteFile() {
        return concreteFile;
    }

    public void setConcreteFile(java.io.File concreteFile) {
        this.concreteFile = concreteFile;
    }

    @Override
    public String toString() {
        return name;
    }
}
