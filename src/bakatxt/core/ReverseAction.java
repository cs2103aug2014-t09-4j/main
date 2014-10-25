//@author A0115160X
package bakatxt.core;

import java.util.Stack;

public class ReverseAction implements ReverseActionInterface {
    private static Stack<UserInput> undoStack;
    private static Stack<UserInput> redoStack;

    public ReverseAction() {
        undoStack = new Stack<UserInput>();
        redoStack = new Stack<UserInput>();
    }

    /**
     * Stores the executed command and task.
     * 
     * @param input
     *            a UserInput that contains the execution methods
     * @return <code>true</code> if a command is executed
     */
    @Override
    public boolean execute(UserInput input) {
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
            UserInput action = undoStack.pop();
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
            UserInput action = redoStack.pop();
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
