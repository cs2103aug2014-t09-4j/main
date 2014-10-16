package bakatxt.core;

import java.util.ArrayList;
import java.util.LinkedList;

import bakatxt.gui.BakaUI;

public class BakaProcessor implements BakaProcessorInterface {

    private static Database _database;
    private static BakaParser _parser;
    private static LinkedList<Task> _displayTasks;

    enum CommandType {
        ADD, DELETE, SHOW, DISPLAY, CLEAR, DEFAULT, REMOVE, EDIT, EXIT
    }

    public BakaProcessor() {
        _database = Database.getInstance();
        _parser = new BakaParser();
        _displayTasks = _database.getAllTasks();
    }

    @Override
    public String addTask(String input) {
        Task toAdd = _parser.add(input);
        _database.add(toAdd);
        String output = toAdd.toDisplayString();
        _displayTasks = _database.getAllTasks();

        return output;
    }

    @Override
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

    @Override
    public void displayTask() {
        _displayTasks = _database.getAllTasks();
    }

    @Override
    public LinkedList<Task> getAllTasks() {
        return _displayTasks;
    }

    @Override
    public void clearTask() {
        _database.clear();
        _displayTasks = _database.getAllTasks();
    }

    @Override
    public void editTask(String input) {
        String index = _parser.getString(input).trim();
        int trueIndex = Integer.valueOf(index.trim());
        _displayTasks = _database.getAllTasks();
        Task target = _displayTasks.get(trueIndex - 1);
        _database.delete(target);
        Task toAdd = _parser.add("add" + BakaUI.getInputText());
        _database.add(toAdd);
        _displayTasks = _database.getAllTasks();
    }

    @Override
    public void exitProg() {
        _database.close();
        System.exit(0);
    }

    @Override
    public String executeCommand(String input) {

        String command = _parser.getCommand(input);
        CommandType commandType;

        try {
            commandType = CommandType.valueOf(command);
        } catch (IllegalArgumentException e) {
            commandType = CommandType.DEFAULT;
        }

        String output = null;
        switch (commandType) {

            case ADD :
                output = addTask(input);
                break;

            case REMOVE :

            case DELETE :
                deleteTask(input);
                break;

            case SHOW :

            case DISPLAY :
                displayTask();
                break;

            case CLEAR :
                clearTask();
                break;

            case EDIT :
                editTask(input);
                break;

            case EXIT :
                break;

            case DEFAULT :
                output = addTask(input);
                break;

            default :
                break;
        }
        return output;
    }

}
