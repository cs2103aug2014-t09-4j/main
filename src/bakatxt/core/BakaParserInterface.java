package bakatxt.core;

import java.util.ArrayList;

// Interface for Parser

public interface BakaParserInterface {
    public String getCommand(String input);

    public Task add(String input);

    public String getString(String input);

    public ArrayList<Integer> getIndexList(String input);
}
