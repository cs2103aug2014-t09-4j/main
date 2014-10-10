package bakatxt.core;

import java.awt.GraphicsEnvironment;
import java.util.LinkedList;
import java.util.Scanner;

import bakatxt.gui.BakaUI;

public class BakaTxtMain {

    private static final String MESSAGE_WELCOME = "Welcome To BakaTxt!";
    private static final String MESSAGE_BYEBYE = "Bye bye!";
    private static final String MESSAGE_ENTER_COMMAND = "Please enter command: ";
    private static final String MESSAGE_INVALID_COMMAND = "Invalid Command!";

    enum CommandType {
        ADD, DELETE, DISPLAY, CLEAR, DEFAULT, EDIT, EXIT
    }

    private static Scanner _sc;
    private static BakaParser _parser;
    private static Database _database;
    private static LinkedList<Task> _displayTasks;

    // ds kludge for demo
    private static boolean withinDelete;
    private static String titleDelete;

    // end demo code

    public BakaTxtMain() {

        _sc = new Scanner(System.in);
        _parser = BakaParser.getInstance();
        _database = Database.getInstance();
        _displayTasks = _database.getAllTasks();

        // ds kludge for demo
        withinDelete = false;
        titleDelete = null;
        // end demo code
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
        // ds kludge for demo
        if (withinDelete) {
            input = "delete " + input;
        }
        // end demo code
        String command = _parser.getCommand(input);
        CommandType commandType;

        try {
            commandType = CommandType.valueOf(command);
        } catch (IllegalArgumentException e) {
            commandType = CommandType.DEFAULT;
            System.out.println(MESSAGE_INVALID_COMMAND);
        }

        String output = null;
        switch (commandType) {

            case ADD :
                output = addTask(input, output);
                break;

            case DELETE :
                deleteTask(input);
                break;

            case DISPLAY :
                displayTask();
                break;

            case CLEAR :
                clearTask();
                break;

            case EDIT :
                editTask();
                break;

            case EXIT :
                exitProg();
                break;

            case DEFAULT :
            default :
                break;
        }
        return output;

    }

    private static void exitProg() {
        System.out.println(MESSAGE_BYEBYE);
        _database.close();
        System.exit(0);
    }

    private static void clearTask() {
        _database.clear();
        _displayTasks = _database.getAllTasks();
    }

    private static void displayTask() {
        _displayTasks = _database.getAllTasks();
    }

    private static void deleteTask(String input) {
        if (!withinDelete) {
            String titleName = _parser.getString(input).trim();
            _displayTasks = _database.getTaskWithTitle(titleName);
            // System.out.println(_displayTasks.toString());
            withinDelete = true;
            titleDelete = titleName;
            if (_displayTasks.size() == 0) {
                withinDelete = false;
                titleDelete = null;
            }
        } else {
            _displayTasks = _database.getTaskWithTitle(titleDelete);
            // System.out.println(_displayTasks.toString());
            String index = _parser.getString(input).trim();
            int trueIndex = Integer.valueOf(index.trim());
            Task target = _displayTasks.get(trueIndex - 1);
            String targetTitle = target.getTitle();
            targetTitle = targetTitle.substring(targetTitle.indexOf(" ") + 1)
                    .trim();
            target.addTitle(targetTitle);
            // System.out.println(target);
            boolean deleted = _database.delete(target);
            // System.out.println(deleted);
            _displayTasks = _database.getAllTasks();
            withinDelete = false;
            titleDelete = null;
        }
    }

    private static String addTask(String input, String output) {
        Task toAdd = _parser.add(input);
        boolean isAdded = _database.add(toAdd);
        if (isAdded) {
            // TODO something when added
            output = toAdd.toDisplayString();
            _displayTasks = _database.getAllTasks();
        } else {
            // TODO error in adding
        }
        return output;
    }

    private static void editTask() {
        // TODO add lines in to make it work
    }

    public LinkedList<Task> getAllTasks() {
        return _displayTasks;
    }
}
