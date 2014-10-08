package bakatxt.core;

import java.awt.GraphicsEnvironment;
import java.util.LinkedList;
import java.util.Scanner;

import bakatxt.gui.BakaUI;

public class BakaTxtMain {

    private static final String MESSAGE_WELCOME = "Welcome To BakaTxt!";
    private static final String MESSAGE_BYEBYE = "Bye bye!";
    private static final String MESSAGE_ENTER_COMMAND = "Please enter command: ";
    private static final String MESSAGE_ENTER_NUM = "Please enter the number that you wish to delete: ";

    enum CommandType {
        ADD, DELETE, DISPLAY, CLEAR, EXIT;
    }

    private static Scanner _sc;
    private static BakaParser _parser;
    private static Database _database;
    private static String _filename;
    private static LinkedList<Task> _displayTasks;

    // ds kludge for demo
    private static boolean withinDelete;
    private static String titleDelete;

    // end demo code

    public BakaTxtMain() {
        // TODO this is kludge code - to be removed
        _filename = "mytestfile.txt";

        _sc = new Scanner(System.in);
        _parser = new BakaParser();
        _database = new Database(_filename);
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
        CommandType commandType = CommandType.valueOf(command);

        String output = null;
        switch (commandType) {
            case ADD :
                Task toAdd = _parser.add(input);
                boolean isAdded = _database.add(toAdd);
                if (isAdded) {
                    // TODO something when added
                    output = toAdd.toDisplayString();
                    _displayTasks = _database.getAllTasks();
                } else {
                    // TODO error in adding
                }
                break;

            case DELETE :
                // Ki Hyun's codes
                // String titleName = _parser.delete(input);
                // _displayTasks = _database.getTaskWithTitle(titleName);
                // System.out.println(MESSAGE_ENTER_NUM);
                // String inputNumber = _sc.nextLine();
                // int digit = Integer.valueOf(inputNumber);
                //
                // boolean isDeleted = _database.delete(_displayTasks
                // .get(digit - 1));
                //
                // if (isDeleted) {
                // // TODO something when deleted
                //
                // } else {
                // // TODO error in deleting
                // }

                // ds kludge for demo
                if (!withinDelete) {
                    String titleName = _parser.delete(input).trim();
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
                    String index = _parser.delete(input).trim();
                    int trueIndex = Integer.valueOf(index.trim());
                    Task target = _displayTasks.get(trueIndex - 1);
                    String targetTitle = target.getTitle();
                    targetTitle = targetTitle.substring(
                            targetTitle.indexOf(" ") + 1).trim();
                    target.addTitle(targetTitle);
                    // System.out.println(target);
                    boolean deleted = _database.delete(target);
                    // System.out.println(deleted);
                    _displayTasks = _database.getAllTasks();
                    withinDelete = false;
                    titleDelete = null;
                }
                // end demo code

                break;

            case DISPLAY :
                _displayTasks = _database.getAllTasks();
                output = "Get from LinkedList";
                break;

            case CLEAR :
                _database.clear();
                _displayTasks = _database.getAllTasks();
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
