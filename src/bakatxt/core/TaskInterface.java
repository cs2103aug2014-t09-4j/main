//@author A0116320Y
package bakatxt.core;

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

    public Task merge(Task toMerge);

    public void setDone(boolean isDone);

    public void setDeleted(boolean isDeleted);

    public void setFloating(boolean isFloating);

    public void updateOverdueStatus();

    public String toDisplayString();

    public String getKey();
}
