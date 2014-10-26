package bakatxt.core;

import java.util.LinkedList;

public class UserClear extends UserAction {
    private LinkedList<Task> _tasks;

    public UserClear(String command) {
        super(command, null);
        _tasks = super._database.getAllTasks();
    }

    @Override
    public boolean execute() {
        for (Task task : _tasks) {
            super.delete(task);
        }
        return true;
    }

    @Override
    public boolean undo() {
        for (Task task : _tasks) {
            super.add(task);
        }
        return true;
    }

}
