package bakatxt.core;

import java.util.ArrayList;
import java.util.LinkedList;

public class BakaProcessor {

    private static Database _database;
    private static BakaParser _parser;
    private static LinkedList<Task> _displayTasks;
    private static ReverseAction ra = new ReverseAction();

    enum CommandType {
        ADD,
        DELETE,
        SHOW,
        DISPLAY,
        CLEAR,
        DEFAULT,
        REMOVE,
        EDIT,
        UNDO,
        REDO,
        EXIT
    }

    public BakaProcessor() {
        _database = Database.getInstance();
        _parser = new BakaParser();
        _displayTasks = _database.getAllTasks();
    }

    public String addTask(String input) {
        Task toAdd = _parser.add(input);
        _database.add(toAdd);
        String output = toAdd.toDisplayString();
        _displayTasks = _database.getAllTasks();

        return output;
    }

    public void deleteTask(String input) {
        String content = _parser.getString(input).trim();
        ArrayList<Integer> listOfIndex = _parser.getIndexList(content);
        for (int i = 0; i < listOfIndex.size(); i++) {
            int trueIndex = listOfIndex.get(i);
            Task target = _displayTasks.get(trueIndex - 1);
            _database.delete(target);
        }

        _displayTasks = _database.getAllTasks();

    }

    public void displayTask() {
        _displayTasks = _database.getAllTasks();
    }

    public LinkedList<Task> getAllTasks() {
        return _displayTasks;
    }

    public void clearTask() {
        _database.clear();
        _displayTasks = _database.getAllTasks();
    }

    public void editTask(String input) {
        String index = _parser.getString(input).trim();
        int trueIndex = Integer.valueOf(index.trim());
        _displayTasks = _database.getAllTasks();
        Task target = _displayTasks.get(trueIndex - 1);
        _database.delete(target);
        Task toAdd = _parser.add("add");
        _database.add(toAdd);
        _displayTasks = _database.getAllTasks();
    }

    public void exitProg() {
        _database.close();
        System.exit(0);
    }

    public String executeCommand(String input) {

        String command = _parser.getCommand(input);
        CommandType commandType;

        try {
            commandType = CommandType.valueOf(command);
        } catch (IllegalArgumentException e) {
            commandType = CommandType.DEFAULT;
        }

        UserInput inputCmd = null;
        Task task = null;
        switch (commandType) {

            case ADD :
                // output = addTask(input);
                task = _parser.add(input);
                inputCmd = new UserInput(command, task);
                ra.execute(inputCmd);
                _database.getAllTasks();
                break;

            case REMOVE :
            case DELETE :
                // deleteTask(input);
                String content = _parser.getString(input).trim();
                ArrayList<Integer> listOfIndex = _parser.getIndexList(content);
                for (int i = 0; i < listOfIndex.size(); i++) {
                    int trueIndex = listOfIndex.get(i);
                    _displayTasks = _database.getAllTasks();
                    task = _displayTasks.get(trueIndex - 1);
                    inputCmd = new UserInput(command, task);
                    ra.execute(inputCmd);
                }

                break;

            case SHOW :
            case DISPLAY :
                displayTask();
                break;

            case CLEAR :
                clearTask();
                break;

            case EDIT :
                // editTask(input);
                String index = _parser.getString(input).trim();
                int trueIndex = Integer.valueOf(index.trim());
                _displayTasks = _database.getAllTasks();
                task = _displayTasks.get(trueIndex - 1);
                // temporarily set it to null
                String newContent = null;
                Task toEdit = _parser.add("add" + newContent);
                inputCmd = new UserInput(command, task, toEdit);
                ra.execute(inputCmd);
                break;

            case UNDO :
                ra.undo();
                break;

            case REDO :
                ra.redo();
                break;

            case EXIT :
                _database.close();
                System.exit(0);
                break;

            case DEFAULT :
                // addTask(input);
                task = _parser.add("add " + input);
                inputCmd = new UserInput("add", task);
                ra.execute(inputCmd);
                break;

            default :
                break;
        }

        _displayTasks = _database.getAllTasks();
        return null;
    }
}
