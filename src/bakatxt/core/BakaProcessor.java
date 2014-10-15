package bakatxt.core;

import java.util.ArrayList;
import java.util.LinkedList;

public class BakaProcessor implements BakaProcessorInterface {

    private static Database _database;
    private static BakaParser _parser;
    private static LinkedList<Task> _displayTasks;

    enum CommandType {
        ADD, DELETE, DISPLAY, CLEAR, DEFAULT, EDIT, EXIT
    }

    public BakaProcessor() {
        _database = Database.getInstance();
        _parser = BakaParser.getInstance();
        _displayTasks = _database.getAllTasks();
    }

    @Override
    public String addTask(String input, String output) {
        Task toAdd = _parser.add(input);
        _database.add(toAdd);
        output = toAdd.toDisplayString();
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
        // TODO Auto-generated method stub

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
                editTask(input);
                break;

            case EXIT :
                break;

            case DEFAULT :
            default :
                break;
        }
        return output;
    }

}
