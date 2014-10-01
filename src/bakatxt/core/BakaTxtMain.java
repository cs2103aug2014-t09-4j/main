package bakatxt.core;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BakaTxtMain {

    enum CommandType {
        ADD, DELETE, DISPLAY, EXIT
    }

    private static final String MESSAGE_FILENAME = "Please input a filename: ";
    private static final String MESSAGE_INVALID_FORMAT = "Invalid command format";
    private static final String MESSAGE_WELCOME = "Welcome To BakaTxt!";
    private static final String MESSAGE_INVALID_COMMAND = "Unrecognized command type";
    private static final String MESSAGE_ENTER_COMMAND = "Please enter command: ";
    private static final String MESSAGE_BYEBYE = "Bye bye!";

    private static Scanner _sc;
    private static BakaTxtSession _session;
    private static String _filename;

    public static void main(String[] args) {
        String command;

        _sc = new Scanner(System.in);

        initializeFile();
        _session = new BakaTxtSession(_filename);

        while (true) {
            System.out.print(MESSAGE_ENTER_COMMAND);
            command = _sc.nextLine();
            String output = executeCommand(command);
            System.out.println(output);
        }
    }

    private static void initializeFile() {
        System.out.print(MESSAGE_FILENAME);
        _filename = _sc.nextLine();

        while (!checkFileName()) {
            System.out.print(MESSAGE_FILENAME);
            _filename = _sc.nextLine();
        }

        System.out.println(MESSAGE_WELCOME);
    }

    private static boolean checkFileName() {
        if (!Files.isReadable(Paths.get(_filename))) {
            return false;
        }
        return true;
    }

    private static String getFirstWord(String command) {
        StringTokenizer tokenizedCommand = new StringTokenizer(command);
        String commandLine = tokenizedCommand.nextToken();
        return commandLine;
    }

    public static String executeCommand(String userCommand) {
        if (userCommand.trim().equals("")) {
            return invalidFormat(userCommand);
        }

        String output;

        switch (CommandType.valueOf(getFirstWord(userCommand).toUpperCase())) {
            case ADD :
                output = _session.add(removeFirstWord(userCommand));
                break;
            case DELETE :
                output = _session.delete(removeFirstWord(userCommand));
                break;
            case DISPLAY :
                output = _session.display(removeFirstWord(userCommand));
                break;
            case EXIT :
                System.out.println(MESSAGE_BYEBYE);
                _session.exit();
                System.exit(0);
                //$FALL-THROUGH$ (to remove fall-through warning in eclipse)
            default :
                output = MESSAGE_INVALID_COMMAND;
        }

        return output;
    }

    private static String invalidFormat(String userCommand) {
        return String.format(MESSAGE_INVALID_FORMAT, userCommand);
    }

    private static String removeFirstWord(String command) {
        return command.replace(getFirstWord(command), "").trim();
    }
}
