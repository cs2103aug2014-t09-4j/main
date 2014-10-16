package bakatxt.core;

import java.util.LinkedList;

public interface BakaProcessorInterface {
    public String executeCommand(String input);

    public String addTask(String input, String output);

    public void deleteTask(String input);

    public void displayTask();

    public void editTask(String input);

    public LinkedList<Task> getAllTasks();

    public void clearTask();

    public void exitProg();
}
