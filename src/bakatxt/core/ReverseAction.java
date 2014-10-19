package bakatxt.core;

import java.util.Stack;

public class ReverseAction implements ReverseActionInterface {
    private static Stack<UserInput> undoStack;
    private static Stack<UserInput> redoStack;

    public ReverseAction() {
        undoStack = new Stack<UserInput>();
        redoStack = new Stack<UserInput>();
    }

    @Override
    public boolean execute(UserInput input) {
        // TODO Auto-generated method stub
        boolean status = input.execute(); // execute command
        if (status) {
            undoStack.push(input);
        }
        return status;
    }

    @Override
    public boolean undo() {
        // TODO Auto-generated method stub
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

    @Override
    public boolean redo() {
        // TODO Auto-generated method stub
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
