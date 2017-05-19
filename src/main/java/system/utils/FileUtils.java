package system.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import system.entities.*;
import system.exceptions.ReadFailureException;
import system.exceptions.WriteFailureException;

import java.io.*;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static system.utils.FileUtilsHelper.*;
import static system.utils.Utils.ROOT_DIR_PATH;

/**
 * Created by Dan on 4/25/2017.
 */
public interface FileUtils {

    // Constants
    Type userListType = new TypeToken<List<User>>(){}.getType();

    String ENVIRONMENTAL_VARIABLES = "files" + File.separator + "ENVIRONMENTAL_VARIABLES.properties";
    String HOME_DIRECTORIES = "files" + File.separator + "home_directories.txt";
    String ROOT_DIRECTORIES = "files" + File.separator + "root_directories.txt";
    String USERS_FILE_PATH = "files" + File.separator + "users.json";
    String COMMANDS = "commands";

    // File utils functions

    static List<String> readInputFile(File filePath) throws IOException, UncheckedIOException {
        checkNotNull(filePath, "The given input file is null");
        return readInputFile(filePath.toString());
    }

    static List<String> readInputFileNoCheckException(File filePath) {
        try {
            return readInputFile(filePath);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    static List<String> readInputFile(String filePath) throws IOException, UncheckedIOException {
        checkNotNull(filePath, "The given input file is null");
        checkState(new File(filePath).exists(), "The given input file does not exists");

        return Files.lines(Paths.get(filePath))
                    .collect(toList());
    }

    static List<String> readHomeDirectories() {
        List<String> homeSubDirectoriesNames;

        try {
            homeSubDirectoriesNames = readInputFile(getFileFromResources(HOME_DIRECTORIES));
        } catch (IOException e) {
            homeSubDirectoriesNames = Collections.emptyList();
        }

        return homeSubDirectoriesNames;
    }

    static List<User> readUsersFromFile() throws ReadFailureException {
        File usersFile = getFileFromResources(USERS_FILE_PATH);

        try(Reader reader = new InputStreamReader(new FileInputStream(usersFile), "UTF-8")){
            return new Gson().fromJson(reader, userListType);

        } catch (IOException e) {
            throw new ReadFailureException(e.getMessage());
        }
    }

    static Directory readRootDir() {
        Directory rootDir;
        File concreteRootDir = getFileFromResources(ROOT_DIR_PATH);

        if (!concreteRootDir.exists()) {
            return null;
        }

        rootDir = new Directory(ROOT_DIR_PATH);
        makeConcreteDir(rootDir, concreteRootDir);

        return rootDir;
    }

    static Map<String, String> readEnvironmentalVars() throws ReadFailureException {
        File propertiesFile = getFileFromResources(ENVIRONMENTAL_VARIABLES);
        return readPropertiesFile(propertiesFile);
    }

    static void saveEnvironmentVar(Map<String, String> environmentalVars) throws WriteFailureException {
        File propertiesFile = getFileFromResources(ENVIRONMENTAL_VARIABLES);
        savePropertiesFile(environmentalVars, propertiesFile);
    }

    static List<String> getCommandsFromPath(String $PATH) {
        if ($PATH == null || $PATH.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<String> commandsLocations = Arrays.asList($PATH.split(":"));

        return commandsLocations.stream()
                                .map(FileUtils::convertPathToCommands)
                                .flatMap(commands -> commands.stream())
                                .distinct()
                                .collect(toList());
    }

    static List<String> convertPathToCommands(String locationPath) {
        String separator = File.separator + File.separator; // use to fix regex issues
        File rootDir = getFileFromResources(ROOT_DIR_PATH);

        return Stream.of(locationPath)
                     .map(location -> location.replaceAll("/", separator) + File.separator + COMMANDS)
                     .map(location -> new File(rootDir, location))
                     .filter(File::exists)
                     .map(FileUtils::readInputFileNoCheckException)
                     .findFirst()
                     .orElseGet(Collections::emptyList);
    }
}