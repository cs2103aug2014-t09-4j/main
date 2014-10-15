package bakatxt.core;

import java.util.LinkedList;

public interface BakaProcessorInterface {
    public String addTask(String input);

    public void deleteTask(String input);

    public LinkedList<Task> displayTask();

    public void editTask(String input);
}
