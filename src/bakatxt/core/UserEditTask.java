//@author A0116320Y
package bakatxt.core;

public class UserEditTask extends UserAction {

    private Task _edited;

    /**
     * 
     * @param command
     *            <code>String</code> of command edit
     * @param task
     *            is a specific task that is executed
     * @param edited
     *            is a specific task that is edited
     */
    public UserEditTask(String command, Task task, Task edited) {
        super(command, task);
        _edited = edited;
    }

    /**
     * @return <code>true</code> when the original task is deleted and edited
     *         task is added
     */
    @Override
    public boolean execute() {
        super.delete(_edited);
        super.delete(_task);
        return super.add(_edited);
    }

    /**
     * @return <code>true</code> when the original task is added and edited task
     *         is deleted
     */
    @Override
    public boolean undo() {
        boolean deleteEdited = super.delete(_edited);
        boolean addOriginal = super.add(_task);
        return deleteEdited && addOriginal;
    }

}
