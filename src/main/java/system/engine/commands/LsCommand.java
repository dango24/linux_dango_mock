package system.engine.commands;


import system.engine.Shell;
import system.engine.commands.infrastructre.ShellCommand;
import system.exceptions.IllegalOperationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Created by Dan on 3/20/2017.
 */
public class LsCommand extends ShellCommand {

    // Constants
    private final static int NUM_OF_ENTITIES_PER_LINE = 8;

    // Constructor
    public LsCommand(Shell shell, List<String> params) {
        super(shell, params);
    }

    @Override
    public void execute() throws IllegalOperationException {
        List<String> dirContent;

        if (shell.getWorkingDirectory().isEmpty()) {
            return;
        }

        dirContent = Arrays.asList(shell.getWorkingDirectory().toString().split(" "));
        Collections.sort(dirContent);

        if (dirContent.size() <= NUM_OF_ENTITIES_PER_LINE) {
            System.out.println(createOneLineOutput(dirContent));
        } else {
            createMultiLineOutput(dirContent).stream()
                                             .map(this::createOneLineOutput)
                                             .forEach(System.out::println);
        }
    }

    private List<List<String>> createMultiLineOutput(List<String> dirContent) {
        int numOfLines = dirContent.size()/NUM_OF_ENTITIES_PER_LINE;

        if (dirContent.size()%NUM_OF_ENTITIES_PER_LINE > 0) {
            numOfLines++;
        }

        return IntStream.iterate(0, index -> index+NUM_OF_ENTITIES_PER_LINE)
                         .limit(numOfLines)
                         .mapToObj(index -> getSubList(dirContent, index))
                         .collect(toList());
    }
    
    private List<String> getSubList(List<String> dirContent, int index) {
        int end = Math.min(index + NUM_OF_ENTITIES_PER_LINE, dirContent.size());
        return dirContent.subList(index, end);
    }

    private String createOneLineOutput(List<String> dirContent) {
        return dirContent.stream()
                         .collect(joining(" "));
    }
}
