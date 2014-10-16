package bakatxt.core;

import java.util.LinkedList;

public class UserInput implements UserInputInterface {

    private static final String SPACE = " ";

    private Task _task;
    private Task _edited;
    private String _command;
    private boolean _flag;
    private Database _database;

    public UserInput(String command, Task task, Task edited) {
        _command = command.toUpperCase();
        _task = task;
        _edited = edited;
        _database = Database.getInstance();
    }

    public UserInput(String command, Task task) {
        _command = command.toUpperCase();
        _task = task;
        _edited = null;
        _database = Database.getInstance();
    }

    public UserInput(String command, Task task, boolean flag) {
        _command = command.toUpperCase();
        _task = task;
        _flag = flag;
        _database = Database.getInstance();
    }

    @Override
    public void execute() {
        switch (_command) {
            case "ADD" :
                add(_task);
                break;
            case "DELETE" :
                delete(_task);
                break;
            case "EDIT" :
                delete(_task);
                add(_edited);
                break;
            case "FLOATING" :
                setFloat(_task, _flag);
                break;
            case "DONE" :
                setDone(_task, _flag);
                break;
            default :
                break;
        }
    }

    @Override
    public void undo() {
        switch (_command) {
            case "ADD" :
                delete(_task);
                break;
            case "DELETE" :
                add(_task);
                break;
            case "EDIT" :
                delete(_edited);
                add(_task);
                break;
            case "FLOATING" :
                setFloat(_task, !_flag);
                break;
            case "DONE" :
                setDone(_task, !_flag);
                break;
            default :
                break;
        }
    }

    private void setDone(Task task, boolean flag) {
        _database.setDone(task, flag);
    }

    private void setFloat(Task task, boolean flag) {
        _database.setFloating(task, flag);
    }

    private void delete(Task task) {
        _database.delete(task);
    }

    private void add(Task task) {
        _database.add(task);
    }

    @Override
    public String commandString() {
        String output = _command + SPACE + _task.getTitle();
        return output;
    }

    @Override
    public String undoCommandString() {
        String output = null;
        switch (_command) {
            case "ADD" :
                output = "DELETE";
                break;
            case "DELETE" :
                output = "ADD";
                break;
            default :
                output = _command;
        }
        output += SPACE + _task.getTitle();
        return output;
    }

    @Override
    public LinkedList<Task> getDisplayTasks() {
        return _database.getAllTasks();
    }

}
