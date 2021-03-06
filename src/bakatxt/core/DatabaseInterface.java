//@author A0116320Y
package bakatxt.core;

import java.io.IOException;
import java.util.LinkedList;

// Interface for Database to write to textfile

public interface DatabaseInterface {

    public String getFileName();

    public boolean add(Task task);

    public boolean delete(Task task);

    public boolean setDone(Task task, boolean isDone);

    public boolean setFloating(Task task, boolean isFloating)
            throws IOException;

    public void close();

    public LinkedList<Task> getTaskWithTitle(String title);

    public LinkedList<Task> getTasksWithDate(String key);

    public LinkedList<Task> getAllTasks();

    public LinkedList<Task> getWeekTasks();

    public LinkedList<Task> getDoneTasks();

    public void updateLocale(String locale);
    
    public void updateTheme(String theme);

    public void updateDoneView(boolean isViewingDone);
}
