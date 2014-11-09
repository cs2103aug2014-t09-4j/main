//@author A0116320Y
package bakatxt.core;

import java.util.LinkedList;

public interface UserActionInterface {

    public boolean execute();

    public boolean undo();

    @Deprecated
    String commandString();

    @Deprecated
    String undoCommandString();

    @Deprecated
    LinkedList<Task> getDisplayTasks();

}
