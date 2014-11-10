//@author A0116320Y
package bakatxt.core;

public class UserEditStatus extends UserAction {
    private boolean _flag;

    public UserEditStatus(String command, Task task, boolean flag) {
        super(command, task);
        _flag = flag;
    }

    /**
     * 
     * @return <code>true</code> when the status of the task is changed
     */
    @Override
    public boolean execute() {
        switch (super._command) {
            case "FLOATING" :
                return setFloat(super._task, _flag);
            case "DONE" :
                return setDone(super._task, _flag);
            default :
                return false;
        }
    }

    /**
     * @return <code>true</code> when the status of the task is changed
     */
    @Override
    public boolean undo() {
        switch (super._command) {
            case "FLOATING" :
                return setFloat(super._task, !_flag);
            case "DONE" :
                return setDone(super._task, !_flag);
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
     * 
     * @return <code>true</code> if task status is changed, <code>false</code>
     *         otherwise
     */
    private boolean setDone(Task task, boolean flag) {
        return _database.setDone(task, flag);
    }

    /**
     * 
     * @param task
     *            is a specific floating task
     * @param flag
     *            is <code>true</code> to set the task to floating
     * 
     * @return <code>true</code> if task status is changed, <code>false</code>
     *         otherwise
     */
    private boolean setFloat(Task task, boolean flag) {
        return _database.setFloating(task, flag);
    }
}
