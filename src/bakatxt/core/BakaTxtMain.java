package bakatxt.core;

import java.awt.GraphicsEnvironment;
import java.util.LinkedList;
import java.util.Scanner;

import bakatxt.gui.BakaUI;

public class BakaTxtMain {

    private static final String MESSAGE_WELCOME = "Welcome To BakaTxt!";
    private static final String MESSAGE_BYEBYE = "Bye bye!";
    private static final String MESSAGE_ENTER_COMMAND = "Please enter command: ";

    enum CommandType {
        ADD, DELETE, DISPLAY, EXIT;
    }

    private static Scanner _sc;
    private static BakaParser _parser;
    private static Database _database;
    private static String _filename;
    private static LinkedList<Task> _displayTasks;

    public BakaTxtMain() {
        // TODO this is kludge code - to be removed
        _filename = "mytestfile.txt";
        _sc = new Scanner(System.in);
        _parser = new BakaParser();
        _database = new Database(_filename);
        _displayTasks = null;
    }

    public static void main(String[] args) {

        if (GraphicsEnvironment.isHeadless()) {
            BakaTxtMain thisSession = new BakaTxtMain();

            System.out.println(MESSAGE_WELCOME);

            while (true) {
                System.out.print(MESSAGE_ENTER_COMMAND);
                String input = _sc.nextLine();
                String result = executeCommand(input);
                System.out.println(result);
            }
        }
        BakaUI.startGui();
    }

    public static String executeCommand(String input) {
        String command = _parser.getCommand(input);
        CommandType commandType = CommandType.valueOf(command);

        String output = null;
        switch (commandType) {
            case ADD :
                Task toAdd = _parser.add(input);
                boolean isAdded = _database.add(toAdd);
                if (isAdded) {
                    // TODO something when added
                    output = toAdd.toDisplayString();
                } else {
                    // TODO error in adding
                }
                break;

            case DELETE :
                break;

            case DISPLAY :
                _displayTasks = _database.getAllTasks();
                output = "Get from LinkedList";
                break;

            case EXIT :
                System.out.println(MESSAGE_BYEBYE);
                _database.close();
                System.exit(0);
                break;

            default :
        }

        return output;
    }

    public LinkedList<Task> getAllTasks() {
        return _displayTasks;
    }
}
