package bakatxt.core;

// Interface for Parser

public interface BakaParserInterface {
    public String getFirstWord(String input);

    public Task add(String input);

    public Task delete(String input);

    public Task display(String input);

    // old stuff; not required anymore.
    public String display();

    public String done(String input);

    public String deleteDone();

    public void sort();

    public void exit();

    public String getFileName();

    public String getPrevious();

    public String getTotal();

    public String getUndone();

    public String getDone();

    public String getTask(String title);
}
