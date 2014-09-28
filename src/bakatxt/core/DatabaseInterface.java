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

    public boolean add(Task task);

    public boolean delete(Task task);

    public LinkedList<Task> getTasks(String key);

    public LinkedList<Task> getAllTasks();

    public LinkedList<Task> getAllUndoneTasks();

    public boolean setDone(Task task);

    public boolean setFloating(Task task);

    public void sort();

    public void close();

    public void resetCounters();

    public void removeDone();
}
