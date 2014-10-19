package bakatxt.core;

public interface ReverseActionInterface {

    public boolean execute(UserInput input);

    public boolean undo();

    public boolean redo();
}
