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
    public boolean execute() {
        switch (_command) {
            case "ADD" :
                return add(_task);
            case "DELETE" :
                return delete(_task);
            case "EDIT" :
                delete(_edited);
                delete(_task);
                return add(_edited);
            case "FLOATING" :
                setFloat(_task, _flag);
                return true;
            case "DONE" :
                setDone(_task, _flag);
                return true;
            default :
                return false;
        }
    }

    @Override
    public boolean undo() {
        switch (_command) {
            case "ADD" :
                return delete(_task);
            case "DELETE" :
                return add(_task);
            case "EDIT" :
                boolean deleteEdited = delete(_edited);
                boolean addOriginal = add(_task);
                return deleteEdited && addOriginal;
            case "FLOATING" :
                setFloat(_task, !_flag);
                return true;
            case "DONE" :
                setDone(_task, !_flag);
                return true;
            default :
                return false;
        }
    }

    private void setDone(Task task, boolean flag) {
        _database.setDone(task, flag);
    }

    private void setFloat(Task task, boolean flag) {
        _database.setFloating(task, flag);
    }

    private boolean delete(Task task) {
        return _database.delete(task);
    }

    private boolean add(Task task) {
        return _database.add(task);
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
