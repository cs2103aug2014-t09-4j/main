package bakatxt.core;

import java.io.IOException;
import java.util.LinkedList;

public interface UserInputInterface {

    public void execute() throws IOException;

    public void undo() throws IOException;

    String commandString();

    String undoCommandString();

    LinkedList<Task> getDisplayTasks();

}
