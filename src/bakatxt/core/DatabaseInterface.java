package bakatxt.core;

import java.util.LinkedList;

// Interface for Database to write to textfile

public interface DatabaseInterface {
    public String setFile(String fileName);

    public String getFileName();

    public Task getPrevious();

    public String getTotalCount();

    public String getDoneCount();

    public String getUndoneCount();

    public void resetCounters();

    public boolean add(Task task);

    public boolean delete(Task task);

    public boolean setDone(Task task, boolean isDone);

    public boolean setFloating(Task task, boolean isFloating);

    public void close();

    public void removeDone();

    public LinkedList<Task> getTaskWithTitle(String title);

    public LinkedList<Task> getTasksWithDate(String key);

    public LinkedList<Task> getAllTasks();

    public LinkedList<Task> getAllUndoneTasks();

    public void clear();
}
