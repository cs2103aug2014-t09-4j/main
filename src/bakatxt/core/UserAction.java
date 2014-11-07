//@author A0116320Y
package bakatxt.core;

import java.util.LinkedList;

public class UserAction implements UserActionInterface {

    private static final String SPACE = " ";

    protected Task _task;
    protected String _command;
    protected Database _database;

    /**
     * Initialize an instance of UserAction with the relevant command and task
     * associated
     * 
     * @param command
     *            <code>String</code> of the user command
     * @param task
     *            associated target task of the command
     */
    public UserAction(String command, Task task) {
        _command = command.toUpperCase();
        _task = task;
        _database = Database.getInstance();
    }

    /**
     * Executes the command that is called.
     * 
     * @return <code>true</code> when the command is successfully executed
     */
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

    /**
     * Executes the command that is opposite from the previous command called.
     * 
     * @return <code>true</code> when the command is successfully executed
     */
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

    /**
     * 
     * @param task
     *            is a specific task
     * @param flag
     *            is <code>true</code> to set the task to done
     */
    protected void setDone(Task task, boolean flag) {
        _database.setDone(task, flag);
    }

    /**
     * 
     * @param task
     *            is a specific floating task
     * @param flag
     *            is <code>true</code> to set the task to floating
     */
    protected void setFloat(Task task, boolean flag) {
        _database.setFloating(task, flag);
    }

    /**
     * 
     * @param task
     *            is a specific task to be deleted
     * @return <code>true</code> when the task is successfully deleted
     */
    protected boolean delete(Task task) {
        return _database.delete(task);
    }

    /**
     * 
     * @param task
     *            is a specific task to be added
     * @return <code>true</code> when the task is successfully added
     */
    protected boolean add(Task task) {
        return _database.add(task);
    }

    /**
     * @return <code>String</code> of command and title of the task
     */
    @Override
    public String commandString() {
        String output = _command + SPACE + _task.getTitle();
        return output;
    }

    /**
     * @return <code>String</code> of the command that is opposite to the
     *         command executed in the previous action and title of the task
     */
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

    /**
     * @return <code>LinkedList</code> of all of the tasks
     */
    @Override
    public LinkedList<Task> getDisplayTasks() {
        return _database.getAllTasks();
    }

}
