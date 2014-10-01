package bakatxt.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task implements TaskInterface, Comparable<Task> {

    private static final String SPACE = " ";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    private static final String MESSAGE_NO_DATE = "No date is specified for this task.";
    private static final String MESSAGE_NO_TIME = "No time is specified for this task.";
    private static final String MESSAGE_NO_VENUE = "No venue is specified for this task";
    private static final String MESSAGE_NO_DESCRIPTION = "No description is specified for this task";

    private static final String TAG_TAB = "\t";
    private static final String TAG_OPEN = "[";
    private static final String TAG_CLOSE = "]";
    private static final String TAG_TITLE = TAG_OPEN + "TITLE" + TAG_CLOSE;
    private static final String TAG_DATE = TAG_OPEN + "DATE" + TAG_CLOSE;
    private static final String TAG_TIME = TAG_OPEN + "TIME" + TAG_CLOSE;
    private static final String TAG_VENUE = TAG_OPEN + "VENUE" + TAG_CLOSE;
    private static final String TAG_DESCRIPTION = TAG_OPEN + "DESCRIPTION"
            + TAG_CLOSE;
    private static final String TAG_FLOATING = TAG_OPEN + "FLOATING"
            + TAG_CLOSE;
    private static final String TAG_DONE = TAG_OPEN + "DONE" + TAG_CLOSE;
    private static final String TAG_DELETED = TAG_OPEN + "DELETED" + TAG_CLOSE;

    private static final String TAG_TRUE = "true";
    private static final String TAG_NULL = "null";

    private static final String TAG_FLOATING_HEAD = "0000" + SPACE;
    private static final String TAG_DONE_HEAD = "5000" + SPACE;
    private static final String TAG_DELETED_HEAD = "9999" + SPACE;

    private String _title;
    private String _date;
    private String _time;
    private String _venue;
    private String _description; // TODO allow line breaks for description
    private boolean _isFloating;
    private boolean _isDone;
    private boolean _isDeleted;

    public Task() {
        _title = null;
        _date = null;
        _time = null;
        _venue = null;
        _description = null;
        _isDone = false;
        _isFloating = false;
        _isDeleted = false;
    }

    public Task(String input) {
        List<String> tokenizedInput = Arrays.asList(input.split("\\s+"));
        if (!tokenizedInput.contains(TAG_TITLE)) {
            _title = input;
            _date = null;
            _time = null;
            _venue = null;
            _description = null;
            _isDone = false;
            _isFloating = false;
            _isDeleted = false;
        } else if (isValidTaskString(input)) {
            // TODO fix semi corrupted tasks
            initializeFromDatabaseString(tokenizedInput);
        }
    }

    private static boolean isValidTaskString(String input) {
        ArrayList<String> headers = new ArrayList<String>();
        headers.add(TAG_TITLE);
        headers.add(TAG_DATE);
        headers.add(TAG_TIME);
        headers.add(TAG_VENUE);
        headers.add(TAG_DONE);
        headers.add(TAG_FLOATING);
        headers.add(TAG_DELETED);
        headers.add(TAG_DESCRIPTION);

        for (String header : headers) {
            if (!input.contains(header)) {
                return false;
            }
        }

        return true;
    }

    private void initializeFromDatabaseString(List<String> tokenizedInput) {
        int titleIndex = tokenizedInput.indexOf(TAG_TITLE) + 1;
        StringBuilder title = new StringBuilder();
        for (int i = titleIndex; i < tokenizedInput.indexOf(TAG_DATE); i++) {
            title.append(tokenizedInput.get(i));
            title.append(SPACE);
        }
        _title = title.toString().trim();

        int dateIndex = tokenizedInput.indexOf(TAG_DATE) + 1;
        _date = tokenizedInput.get(dateIndex);

        int timeIndex = tokenizedInput.indexOf(TAG_TIME) + 1;
        _time = tokenizedInput.get(timeIndex);

        int venueIndex = tokenizedInput.indexOf(TAG_VENUE) + 1;
        _venue = tokenizedInput.get(venueIndex);

        int doneIndex = tokenizedInput.indexOf(TAG_DONE) + 1;
        _isDone = tokenizedInput.get(doneIndex).equals(TAG_TRUE);

        int floatingIndex = tokenizedInput.indexOf(TAG_FLOATING) + 1;
        _isFloating = tokenizedInput.get(floatingIndex).equals(TAG_TRUE)
                || (_date.equals(TAG_NULL) && _date.equals(_time));

        int deletedIndex = tokenizedInput.indexOf(TAG_DELETED) + 1;
        _isDeleted = tokenizedInput.get(deletedIndex).equals(TAG_TRUE);

        int descriptionIndex = tokenizedInput.indexOf(TAG_DESCRIPTION) + 1;
        if (descriptionIndex + 1 == tokenizedInput.size()) {
            if (tokenizedInput.get(descriptionIndex).equals(TAG_NULL)) {
                _description = null;
            }
        } else {
            StringBuilder description = new StringBuilder();
            for (int i = descriptionIndex; i < tokenizedInput.size(); i++) {
                description.append(tokenizedInput.get(i));
                description.append(SPACE);
            }
            _description = description.toString().trim();
        }
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public String getDate() {
        if (_date == null) {
            return MESSAGE_NO_DATE;
        }
        return _date;
    }

    @Override
    public String getTime() {
        if (_time == null) {
            return MESSAGE_NO_TIME;
        }
        return _time;
    }

    @Override
    public String getVenue() {
        if (_venue == null) {
            return MESSAGE_NO_VENUE;
        }
        return _venue;
    }

    @Override
    public String getDescription() {
        if (_description == null) {
            return MESSAGE_NO_DESCRIPTION;
        }
        return _description;
    }

    @Override
    public String addTitle(String input) {
        _title = input.trim();
        return _title;
    }

    @Override
    public String addDate(String input) {
        if (input == null) {
            _date = null;
        } else {
            _date = input.trim();
        }
        return _date;
    }

    @Override
    public String addTime(String input) {
        if (input == null) {
            _time = null;
        } else {
            _time = input.trim();
        }
        return _time;
    }

    @Override
    public String addVenue(String input) {
        if (input == null) {
            _venue = null;
        } else {
            _venue = input.trim();
        }
        return _venue;
    }

    @Override
    public String addDescription(String input) {
        if (input == null) {
            _description = null;
        } else {
            _description = input.trim();
        }
        return _description;
    }

    @Override
    public boolean isDone() {
        return _isDone;
    }

    @Override
    public String toDisplayString() {
        StringBuilder task = new StringBuilder();

        task.append(TAG_OPEN + _date + SPACE + _time + TAG_CLOSE + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_TITLE + SPACE + _title + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_DATE + SPACE + _date + SPACE + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_TIME + SPACE + _time + SPACE + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_VENUE + SPACE + _venue + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_DESCRIPTION + SPACE + _description + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_DONE + SPACE + _isDone + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_FLOATING + SPACE + _isFloating + SPACE
                + LINE_SEPARATOR);

        return task.toString();
    }

    @Override
    public String toString() {
        StringBuilder task = new StringBuilder();

        task.append(TAG_OPEN);

        if (_isDeleted) {
            task.append(TAG_DELETED_HEAD);
        }

        if (_isDone) {
            task.append(TAG_DONE_HEAD);
        }

        if (_isFloating) {
            task.append(TAG_FLOATING_HEAD);
        }

        task.append(_date + SPACE + _time + TAG_CLOSE + SPACE);
        task.append(TAG_TITLE + SPACE + _title + SPACE);
        task.append(TAG_DATE + SPACE + _date + SPACE);
        task.append(TAG_TIME + SPACE + _time + SPACE);
        task.append(TAG_VENUE + SPACE + _venue + SPACE);
        task.append(TAG_DONE + SPACE + _isDone + SPACE);
        task.append(TAG_FLOATING + SPACE + _isFloating + SPACE);
        task.append(TAG_DELETED + SPACE + _isDeleted + SPACE);
        task.append(TAG_DESCRIPTION + SPACE + _description + SPACE);

        return task.toString();
    }

    @Override
    public void setDeleted(boolean isDeleted) {
        _isDeleted = isDeleted;
    }

    @Override
    public boolean isFloating() {
        return _isFloating;
    }

    @Override
    public void setDone(boolean isDone) {
        _isDone = isDone;
    }

    @Override
    public void setFloating(boolean isFloating) {
        _isFloating = isFloating;
    }

    @Override
    public boolean isDeleted() {
        return _isDeleted;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task)) {
            return false;
        }
        Task task = (Task) obj;
        return this.toString().equals(task.toString());
    }

    @Override
    public int compareTo(Task task) {
        String thisOne = getKey() + getTime();
        String thatOne = task.getKey() + getTime();
        return thisOne.compareTo(thatOne);
    }

    @Override
    public String getKey() {
        StringBuilder key = new StringBuilder();

        if (_isDeleted) {
            key.append(TAG_DELETED_HEAD);
        }

        if (_isDone) {
            key.append(TAG_DONE_HEAD);
        }

        if (_isFloating) {
            key.append(TAG_FLOATING_HEAD);
        }

        key.append(_date);

        return key.toString();
    }
}
