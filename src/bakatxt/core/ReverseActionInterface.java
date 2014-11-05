//@author A0115160X
package bakatxt.core;

public interface ReverseActionInterface {

    public boolean execute(UserAction input);

    public boolean undo();

    public boolean redo();
}
