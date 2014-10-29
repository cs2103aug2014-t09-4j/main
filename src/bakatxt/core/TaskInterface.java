package bakatxt.core;

// Interface for a Task object

public interface TaskInterface {
    public String getTitle();

    public String getDate();

    public String getFormattedDate();

    public String getTime();

    public String getEndTime();

    public String getVenue();

    public String getDescription();

    public String setTitle(String input);

    public String setDate(String input);

    public String setTime(String input);

    public String setEndTime(String input);

    public String setVenue(String input);

    public String setDescription(String input);

    public boolean isDone();

    public boolean isFloating();

    public boolean isDeleted();

    public boolean isOverdue();

    // These are methods used by the Database

    public void setDone(boolean isDone);

    public void setDeleted(boolean isDeleted);

    public void setFloating(boolean isFloating);

    public String toDisplayString();

    public String getKey();
}
