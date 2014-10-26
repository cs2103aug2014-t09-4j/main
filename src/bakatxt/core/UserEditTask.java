package bakatxt.core;

public class UserEditTask extends UserAction {

    private Task _edited;

    public UserEditTask(String command, Task task, Task edited) {
        super(command, task);
        _edited = edited;
    }

    @Override
    public boolean execute() {
        super.delete(_edited);
        super.delete(_task);
        return super.add(_edited);
    }

    @Override
    public boolean undo() {
        boolean deleteEdited = super.delete(_edited);
        boolean addOriginal = super.add(_task);
        return deleteEdited && addOriginal;
    }

}
