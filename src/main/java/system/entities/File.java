package system.entities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static system.utils.FileUtils.readInputFile;

/**
 * Created by Dan on 3/20/2017.
 */
public class File extends FileSystemEntity {

    // Constructor
    public File(String name) {
        this(name, null, null);
    }

    public File(String name, Directory parent, java.io.File concreteFile) {
        super(name, parent, concreteFile);
    }

    // Methods

    @Override
    public boolean isEmpty() {
        try {
            return readInputFile(getConcreteFile()).isEmpty();
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public void deleteContent() {
        try (PrintWriter writer = new PrintWriter(getConcreteFile())){
            writer.print("");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
