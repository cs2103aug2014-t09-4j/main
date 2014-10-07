package bakatxt.core;

// Interface for Parser

public interface BakaParserInterface {
    public String getCommand(String input);

    public Task add(String input);

    public String delete(String input);

    public Task display(String input);

}
