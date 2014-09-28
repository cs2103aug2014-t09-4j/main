package bakatxt.core;
// Interface for a BakaTxtSession

public interface BakaTxtSessionInterface {
    public String add(String input);

    public String delete(String input);

    public String display(String input);

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
