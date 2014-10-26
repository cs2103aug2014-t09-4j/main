//@author A0115160X
package bakatxt.core;

import java.util.Stack;

public class ReverseAction implements ReverseActionInterface {
    private static Stack<UserAction> undoStack;
    private static Stack<UserAction> redoStack;

    public ReverseAction() {
        undoStack = new Stack<UserAction>();
        redoStack = new Stack<UserAction>();
    }

    /**
     * Stores the executed command and task.
     * 
     * @param input
     *            a UserAction that contains the execution methods
     * @return <code>true</code> if a command is executed
     */
    @Override
    public boolean execute(UserAction input) {
        boolean status = input.execute(); // execute command
        if (status) {
            undoStack.push(input);
        }
        return status;
    }

    /**
     * Undo the previous action.
     * 
     * @return <code>true</code> if undo is executed
     */
    @Override
    public boolean undo() {
        boolean status = false;
        if (!undoStack.isEmpty()) {
            UserAction action = undoStack.pop();
            status = action.undo();
            if (!status) {
                undoStack.push(action);
            } else {
                redoStack.push(action);
            }
        }
        return status;
    }

    /**
     * Redo the previous undo action.
     * 
     * @return <code>true</code> if redo is executed
     */
    @Override
    public boolean redo() {
        boolean status = false;
        if (!redoStack.isEmpty()) {
            UserAction action = redoStack.pop();
            status = action.execute();
            if (!status) {
                redoStack.push(action);
            } else {
                undoStack.push(action);
            }
        }
        return status;
    }

}
