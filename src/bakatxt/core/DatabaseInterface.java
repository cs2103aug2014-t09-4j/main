package bakatxt.core;

import java.io.IOException;
import java.util.LinkedList;

// Interface for Database to write to textfile

public interface DatabaseInterface {
    public String setFile(String fileName);

    public String getFileName();

    public Task getPrevious();

    public boolean add(Task task);

    public boolean delete(Task task) throws IOException;

    public boolean setDone(Task task, boolean isDone) throws IOException;

    public boolean setFloating(Task task, boolean isFloating)
            throws IOException;

    public void close() throws IOException;

    public void removeDone() throws IOException;

    public LinkedList<Task> getTaskWithTitle(String title);

    public LinkedList<Task> getTasksWithDate(String key);

    public LinkedList<Task> getAllTasks();

    public LinkedList<Task> getAllUndoneTasks();

    public void clear() throws IOException;
}
