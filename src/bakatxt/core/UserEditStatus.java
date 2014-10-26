package bakatxt.core;

public class UserEditStatus extends UserAction {
    private boolean _flag;

    public UserEditStatus(String command, Task task, boolean flag) {
        super(command, task);
        _flag = flag;
    }

    @Override
    public boolean execute() {
        switch (super._command) {
            case "FLOATING" :
                super.setFloat(_task, _flag);
                return true;
            case "DONE" :
                super.setDone(_task, _flag);
                return true;
            default :
                return false;
        }
    }

    @Override
    public boolean undo() {
        switch (super._command) {
            case "FLOATING" :
                super.setFloat(_task, !_flag);
                return true;
            case "DONE" :
                super.setDone(_task, !_flag);
                return true;
            default :
                return false;
        }
    }
}