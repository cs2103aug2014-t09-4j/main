package bakatxt.core;
// Interface for a Task object

public interface TaskInterface {
    public String getTitle();

    public String getDate();

    public String getTime();

    public String getVenue();

    public String getDescription();

    public String addTitle(String input);

    public String addDate(String input);

    public String addTime(String input);

    public String addVenue(String input);

    public String addDescription(String input);

    public boolean isDone();

    public boolean isFloating();

    public boolean isDeleted();

    // These are methods used by the Database

    public void setDone(boolean isDone);

    public void setDeleted(boolean isDeleted);

    public void setFloating(boolean isFloating);

    public String toDisplayString();

    public String getKey();
}
