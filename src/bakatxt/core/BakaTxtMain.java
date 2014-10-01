package bakatxt.core;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BakaTxtMain {

    enum CommandType {
        ADD, DELETE, DISPLAY, EXIT
    }

    private static Scanner sc;
    private static final String MESSAGE_INVALID_FILE = "Error opening/creating file";
    private static final String MESSAGE_INVALID_FORMAT = "invalid command format";
    private static final String MESSAGE_WELCOME = " Welcome To BakaTxt";
    private static final String MESSAGE_INVALID_COMMAND = "Unrecognized command type";
    private static BakaTxtSession session;

    public static void main(String[] args) {
        String filename;
        String command;

        sc = new Scanner(System.in);

        generateStartMessage();
        filename = sc.nextLine();
        session = new BakaTxtSession(filename);
        if (checkFileName(filename) == true) {
            while (true) {

                command = sc.nextLine();
                executeCommand(command);
            }
        }
        System.out.println(MESSAGE_INVALID_FILE);
    }

    private static void generateStartMessage() {
        System.out.println(MESSAGE_WELCOME);
    }

    private static boolean checkFileName(String filename) {

        if (!Files.isReadable(Paths.get(filename))) {
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

        switch (CommandType.valueOf(getFirstWord(userCommand).toUpperCase())) {
            case ADD :
                return session.add(removeFirstWord(userCommand));
            case DELETE :
                return session.delete(removeFirstWord(userCommand));
            case DISPLAY :
                return session.display(removeFirstWord(userCommand));
            case EXIT :
                System.exit(0);
                //$FALL-THROUGH$ (to remove fall-through warning in eclipse)
            default :
                // throw an error if the command is not recognized
                throw new Error(MESSAGE_INVALID_COMMAND);
        }

    }

    private static String invalidFormat(String userCommand) {
        return String.format(MESSAGE_INVALID_FORMAT, userCommand);
    }

    private static String removeFirstWord(String command) {
        return command.replace(getFirstWord(command), "").trim();
    }
}
