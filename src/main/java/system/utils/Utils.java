package system.utils;

import system.engine.commands.PwdCommand;
import system.entities.Directory;
import system.entities.User;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static system.utils.FileUtils.readHomeDirectories;

/**
 * Created by Dan on 3/21/2017.
 */
public interface Utils {

    // Constans
    String ROOT_DIR_PATH = "root";
    String HOME_DIR_PATH = "home";
    String PARENT_DIR = "..";

    // Functions

    static List<String> createParams(String[] userInput) {
        return IntStream.range(1, userInput.length)
                        .mapToObj(i -> userInput[i])
                        .collect(toList());
    }

    static User getAdminUser(List<User> users) {
        return users.stream()
                    .filter(User::isAdmin)
                    .findFirst()
                    .orElseGet(User::createAdminUser);
    }


    static Directory createHomeDirectory(String homeDirName) {
        Directory homeDir = new Directory(homeDirName);
        List<String> homeSubDirectoriesNames = readHomeDirectories();

        homeSubDirectoriesNames.stream()
                .map(homeSubDirsName -> new Directory(homeSubDirsName, homeDir))
                .forEach(homeDir::addFileSystemEntity);

        return homeDir;
    }

    static String getDirectoryPath(Directory directory) {
        try {
            Method dirPathMethod = PwdCommand.class.getDeclaredMethod("getDirPath", Directory.class);
            dirPathMethod.setAccessible(true);

            return String.valueOf(dirPathMethod.invoke(new PwdCommand(null), directory));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    static Directory getInnerDirectoryFromDirsPath(Directory workingDirectory, List<String> directoriesPath) {
        Directory nextDirectory = workingDirectory;

        for (String dirPath : directoriesPath) {
            nextDirectory = nextDirectory.getInnerDirectory(dirPath);
        }

        return nextDirectory;
    }

    static List<String> separateConcatDirPathToList(String dirPath) {
        return Arrays.asList(dirPath.split("/"));
    }

    static String getDirName(Directory dir) {

        if (dir.getName().equals(ROOT_DIR_PATH)) {
            return "/";
        } else if (dir.getParent().getName().equals(HOME_DIR_PATH)) {
            return "~";
        } else {
            return dir.getName();
        }
    }

}
