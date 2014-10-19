package bakatxt.core;

import java.util.LinkedList;

public interface UserInputInterface {

    public boolean execute();

    public boolean undo();

    String commandString();

    String undoCommandString();

    LinkedList<Task> getDisplayTasks();

}
