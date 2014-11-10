//@author A0116320Y
package bakatxt.core;

import java.util.LinkedList;

public class UserClear extends UserAction {
    private LinkedList<Task> _tasks;

    /**
     * 
     * @param command
     *            <code>String</code> of command clear
     * @param date
     *            code>String</code> of a day or a period to clear the tasks
     */
    public UserClear(String command, String date) {
        super(command, null);

        if (date == null) {
            date = new String();
        }

        date = date.trim();

        switch (date) {
            case "" :
                _tasks = super._database.getTasksWithDate(null);
                break;
            case "week" :
                _tasks = super._database.getWeekTasks();
                break;
            case "all" :
                _tasks = super._database.getAllTasks();
                break;
            case "done" :
                _tasks = super._database.getDoneTasks();
                break;
            default :
                if (command.equals("SEARCH")) {
                    _tasks = super._database.getTaskWithTitle(date);
                } else {
                    BakaParser parser = new BakaParser();
                    date = parser.getDate(date);
                    _tasks = super._database.getTasksWithDate(date);
                }
        }
    }

    /**
     * @return <code>true</code> when all of the tasks are deleted
     */
    @Override
    public boolean execute() {
        for (Task task : _tasks) {
            super.delete(task);
        }
        return true;
    }

    /**
     * @return <code>true</code> when all of the tasks are added
     */
    @Override
    public boolean undo() {
        for (Task task : _tasks) {
            super.add(task);
        }
        return true;
    }

}
