package bakatxt.core;

import java.util.LinkedList;

public class UserClear extends UserAction {
    private LinkedList<Task> _tasks;

    public UserClear(String command, String date) {
        super(command, null);

        if (date == null) {
            date = new String();
        }

        date = date.trim();
        if (date.isEmpty()) {
            _tasks = super._database.getAllTasks();
        } else if (date.equals("week")) {
            _tasks = super._database.getWeekTasks();
        } else {
            _tasks = super._database.getTasksWithDate(date);
        }
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
