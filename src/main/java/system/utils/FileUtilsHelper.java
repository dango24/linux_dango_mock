package system.utils;

import system.entities.Directory;
import system.entities.FileSystemEntity;
import system.exceptions.ReadFailureException;
import system.exceptions.WriteFailureException;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;

/**
 * Created by Dan on 5/3/2017.
 */
class FileUtilsHelper {

    // Constants
    private static String RESOURCE_DIRECTORY_PATH =
            System.getProperty("user.dir") + File.separator +
            "src" + File.separator +
            "main" + File.separator +
            "resources";

    // Functions

    protected static File getFileFromResources(String fileName) {
        return org.apache.commons.io.FileUtils.getFile(new File(RESOURCE_DIRECTORY_PATH), fileName);
    }

    protected static void makeConcreteDir(Directory directory, File concreteFileSystemEntity) {
        FileSystemEntity fileSystemEntity;
        List<File> innerDirFiles = Arrays.asList(concreteFileSystemEntity.listFiles());

        for(File file : innerDirFiles) {
            if (file.isDirectory()) {
                fileSystemEntity = new Directory(file.getName(), directory, file);
                makeConcreteDir((Directory)fileSystemEntity, file);
            } else {
                fileSystemEntity = new system.entities.File(file.getName(), directory, file);
            }

            directory.addFileSystemEntity(fileSystemEntity);
        }
    }


    static Map<String, String> readPropertiesFile(File propertiesFile) throws ReadFailureException {
        Properties prop;

        try (Reader reader = new InputStreamReader(new FileInputStream(propertiesFile), "UTF-8")){

            if (reader != null) {
                prop = new Properties();
                prop.load(reader);
            } else {
                throw new FileNotFoundException("property file '" + propertiesFile + "' not found in the classpath");
            }

            return prop.entrySet()
                    .stream()
                    .collect(toMap(entry -> String.valueOf(entry.getKey()),
                            entry -> String.valueOf(entry.getValue())));
        } catch (IOException e) {
            throw new ReadFailureException("Failed to open properties file: " + propertiesFile + ", " + e.getMessage());
        }
    }


    static void savePropertiesFile(Map<String, String> environmentalVars, File propertiesFile) throws WriteFailureException {
        Properties prop = new Properties();

        try (OutputStream output = new FileOutputStream(propertiesFile)) {

            // set the properties value
            environmentalVars.entrySet().forEach(entry -> prop.setProperty(entry.getKey(), entry.getValue()));

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException e) {
            throw new WriteFailureException("Failed to write properties file, " + e.getMessage());
        }
    }




}
