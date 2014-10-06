package bakatxt.core;

// Interface for Parser

public interface BakaParserInterface {
    public String getCommand(String input);

    public Task add(String input);

    public Task delete(String input);

    public Task display(String input);

}