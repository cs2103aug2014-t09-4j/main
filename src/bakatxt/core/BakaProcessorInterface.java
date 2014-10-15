package bakatxt.core;

import java.io.IOException;
import java.util.LinkedList;

public interface BakaProcessorInterface {
    public String executeCommand(String input) throws IOException;

    public String addTask(String input, String output);

    public void deleteTask(String input) throws IOException;

    public void displayTask();

    public void editTask(String input) throws IOException;

    public LinkedList<Task> getAllTasks();

    public void clearTask() throws IOException;

    public void exitProg() throws IOException;
}
