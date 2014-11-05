//@author A0116320Y
package bakatxt.core;

import java.util.LinkedList;

public class UserAction implements UserActionInterface {

    private static final String SPACE = " ";

    protected Task _task;
    protected String _command;
    protected Database _database;

    public UserAction(String command, Task task) {
        _command = command.toUpperCase();
        _task = task;
        _database = Database.getInstance();
    }

    @Override
    public boolean execute() {
        switch (_command) {
            case "ADD" :
                return add(_task);
            case "DELETE" :
                return delete(_task);
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
            default :
                return false;
        }
    }

    protected void setDone(Task task, boolean flag) {
        _database.setDone(task, flag);
    }

    protected void setFloat(Task task, boolean flag) {
        _database.setFloating(task, flag);
    }

    protected boolean delete(Task task) {
        return _database.delete(task);
    }

    protected boolean add(Task task) {
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
