package bakatxt.core;

import java.util.LinkedList;

public interface UserActionInterface {

    public boolean execute();

    public boolean undo();

    String commandString();

    String undoCommandString();

    LinkedList<Task> getDisplayTasks();

}
