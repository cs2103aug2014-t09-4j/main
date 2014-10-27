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
        boolean isSuccessful = input.execute(); // execute command
        if (isSuccessful) {
            undoStack.push(input);
        }
        return isSuccessful;
    }

    /**
     * Undo the previous action.
     * 
     * @return <code>true</code> if undo is executed
     */
    @Override
    public boolean undo() {
        boolean isSuccessful = false;
        if (!undoStack.isEmpty()) {
            UserAction action = undoStack.pop();
            isSuccessful = action.undo();
            if (!isSuccessful) {
                undoStack.push(action);
            } else {
                redoStack.push(action);
            }
        }
        return isSuccessful;
    }

    /**
     * Redo the previous undo action.
     * 
     * @return <code>true</code> if redo is executed
     */
    @Override
    public boolean redo() {
        boolean isSuccessful = false;
        if (!redoStack.isEmpty()) {
            UserAction action = redoStack.pop();
            isSuccessful = action.execute();
            if (isSuccessful) {
                redoStack.push(action);
            } else {
                undoStack.push(action);
            }
        }
        return isSuccessful;
    }

}
