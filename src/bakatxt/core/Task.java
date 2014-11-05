//@author A0116320Y
package bakatxt.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task implements TaskInterface, Comparable<Task> {

    private static final String SPACE = " ";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");

    private static final String STRING_OVERDUE = "Overdue since: ";
    private static final String TAG_OPEN = "[";
    private static final String TAG_CLOSE = "]";
    private static final String TAG_TITLE = TAG_OPEN + "TITLE" + TAG_CLOSE;
    private static final String TAG_DATE = TAG_OPEN + "DATE" + TAG_CLOSE;
    private static final String TAG_TIME = TAG_OPEN + "TIME" + TAG_CLOSE;
    private static final String TAG_ENDTIME = TAG_OPEN + "ENDTIME" + TAG_CLOSE;
    private static final String TAG_VENUE = TAG_OPEN + "VENUE" + TAG_CLOSE;
    private static final String TAG_DESCRIPTION = TAG_OPEN + "DESCRIPTION"
            + TAG_CLOSE;
    private static final String TAG_FLOATING = TAG_OPEN + "FLOATING"
            + TAG_CLOSE;
    private static final String TAG_DONE = TAG_OPEN + "DONE" + TAG_CLOSE;
    private static final String TAG_DELETED = TAG_OPEN + "DELETED" + TAG_CLOSE;

    private static final String TAG_TRUE = "true";
    private static final String TAG_NULL = "null";
    private static final String TAG_TAB = "\t";

    private static final String TAG_FLOATING_HEAD = "0000" + SPACE;
    private static final String TAG_DONE_HEAD = "5000" + SPACE;
    private static final String TAG_DELETED_HEAD = "9999" + SPACE;

    private String _title;
    private String _date;
    private String _time;
    private String _endTime;
    private String _venue;
    private String _description; // TODO allow line breaks for description
    private boolean _isFloating;
    private boolean _isDone;
    private boolean _isDeleted;

    public Task() {
        _title = null;
        _date = null;
        _time = null;
        _endTime = null;
        _venue = null;
        _description = null;
        _isDone = false;
        _isFloating = false;
        _isDeleted = false;
    }

    public Task(Task task) {
        this(task.toString());
    }

    public Task(String input) {
        List<String> tokenizedInput = Arrays.asList(input.split("\\s+"));
        if (!tokenizedInput.contains(TAG_TITLE)) {
            _title = input;
            _date = null;
            _time = null;
            _endTime = null;
            _venue = null;
            _description = null;
            _isDone = false;
            _isFloating = true;
            _isDeleted = false;
        } else if (isValidTaskString(input)) {
            // TODO fix semi corrupted tasks
            initializeFromDatabaseString(tokenizedInput);
            updateFloatingStatus();
        }
    }

    private static boolean isValidTaskString(String input) {
        ArrayList<String> headers = new ArrayList<String>();
        headers.add(TAG_TITLE);
        headers.add(TAG_DATE);
        headers.add(TAG_TIME);
        headers.add(TAG_ENDTIME);
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
        int timeIndex = tokenizedInput.indexOf(TAG_TIME) + 1;
        int endTimeIndex = tokenizedInput.indexOf(TAG_ENDTIME) + 1;
        int venueIndex = tokenizedInput.indexOf(TAG_VENUE) + 1;
        int doneIndex = tokenizedInput.indexOf(TAG_DONE) + 1;
        int deletedIndex = tokenizedInput.indexOf(TAG_DELETED) + 1;
        int descriptionIndex = tokenizedInput.indexOf(TAG_DESCRIPTION) + 1;

        _date = tokenizedInput.get(dateIndex);
        _time = tokenizedInput.get(timeIndex);
        _endTime = tokenizedInput.get(endTimeIndex);

        for (int i = venueIndex; i < doneIndex - 1; i++) {
            if (_venue == null) {
                _venue = tokenizedInput.get(i);
            } else {
                _venue = _venue + SPACE + tokenizedInput.get(i);
            }
        }
        _isDone = tokenizedInput.get(doneIndex).equals(TAG_TRUE);
        _isFloating = _date.equals(_time);
        _isDeleted = tokenizedInput.get(deletedIndex).equals(TAG_TRUE);

        if (descriptionIndex + 1 == tokenizedInput.size()) {
            if (tokenizedInput.get(descriptionIndex).equals(TAG_NULL)) {
                _description = null;
            } else {
                _description = tokenizedInput.get(descriptionIndex);
            }
        } else {
            StringBuilder description = new StringBuilder();
            for (int i = descriptionIndex; i < tokenizedInput.size(); i++) {
                description.append(tokenizedInput.get(i));
                description.append(SPACE);
            }
            _description = description.toString().trim();
        }

        if (_isDone == false) {
            setOverdueStatus();
        }
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public String getDate() {
        return _date;
    }

    @Override
    public String getFormattedDate() {
        BakaParser parser = new BakaParser();
        return parser.getFormattedDate(_date);
    }

    @Override
    public String getTime() {
        return _time;
    }

    @Override
    public String getEndTime() {
        return _endTime;
    }

    @Override
    public String getVenue() {
        return _venue;
    }

    @Override
    public String getDescription() {
        return _description;
    }

    @Override
    public String setTitle(String input) {
        _title = input.trim();
        return _title;
    }

    @Override
    public String setDate(String input) {
        if (input == null || input.equals("null")) {
            _date = null;
        } else {
            _date = input.trim();
        }
        updateFloatingStatus();
        return _date;
    }

    private void updateFloatingStatus() {
        if (_date == null || _date.equals("null")) {
            if (_time == null || _time.equals("null")) {
                _isFloating = true;
            } else {
                _isFloating = false;
            }
        } else {
            _isFloating = false;
        }
    }

    @Override
    public String setTime(String input) {
        if (input == null || input.equals("null")) {
            _time = null;
        } else {
            _time = input;
        }
        updateFloatingStatus();
        return _time;
    }

    @Override
    public String setEndTime(String input) {
        if (input == null || input.equals("null")) {
            _endTime = null;
        } else {
            if (_time == null) {
                _time = input.trim();
                _endTime = null;
            } else {
                _endTime = input.trim();
            }
        }
        return _endTime;
    }

    @Override
    public String setVenue(String input) {
        if (input == null || input.equals("null")) {
            _venue = null;
        } else {
            _venue = input.trim();
        }
        return _venue;
    }

    @Override
    public String setDescription(String input) {
        if (input == null || input.equals("null")) {
            _description = null;
        } else {
            _description = input.trim();
        }
        if (_isDone == false) {
            setOverdueStatus();
            updateFloatingStatus();
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

        task.append(TAG_TAB + TAG_TITLE + SPACE + _title + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_DATE + SPACE + _date + SPACE + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_TIME + SPACE + _time + SPACE + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_ENDTIME + SPACE + _endTime + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_VENUE + SPACE + _venue + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_DESCRIPTION + SPACE + _description + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_DONE + SPACE + _isDone + SPACE
                + LINE_SEPARATOR);
        task.append(TAG_TAB + TAG_FLOATING + SPACE + _isFloating + SPACE
                + LINE_SEPARATOR);
        task.append(LINE_SEPARATOR);
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
        task.append(TAG_ENDTIME + SPACE + _endTime + SPACE);
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
        if (_isFloating) {
            _date = _time = _endTime = TAG_NULL;
        }
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
        String thisOne = this.getKey() + this.getTime();
        String thatOne = task.getKey() + task.getTime();
        int timeComparison = thisOne.compareTo(thatOne);
        if (timeComparison == 0) {
            return this.getTitle().compareTo(task.getTitle());
        }
        return timeComparison;
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

    @Override
    public boolean isOverdue() {
        if (_description==null || !_description.contains(STRING_OVERDUE)) {
            return false;
        }
        return true;
    }

    private void setOverdueStatus() {
        BakaParser parser = new BakaParser();
        String today = parser.getDate("today");
        _time = parser.getTime(_time);
        _endTime = parser.getTime(_endTime);
        _date = parser.getDate(_date);

        if (_date == null || _date.equals("null") || _isDone) {
            return;
        } 
        
        int dateStatus = today.compareTo(_date);

        if (dateStatus > 0) { // over due date
            setOverdue();
            
        } else if (dateStatus == 0) { // same date

            String timeNow = BakaParser.getCurrentTime();
            int timeNowValue = Integer.valueOf(timeNow);

            if (_time == null || _time.equals(TAG_NULL)) {
                return;
            }

            int timeTaskValue = Integer.valueOf(_time);
            if (timeTaskValue == timeNowValue) { // same time
                return;
                
            } else if (timeTaskValue < timeNowValue) { // overdue time
                
                if (_endTime == null || _endTime.equals(TAG_NULL)) {
                    setOverdue();
                } else {
                    int endTimeTaskValue = Integer.valueOf(_endTime);
                    if (endTimeTaskValue < timeNowValue) {
                        setOverdue();
                    }
                }

            }
        }
    }

    private void setOverdue() {
        if (_description == null) {
            _description = new String();
        }
        _description = STRING_OVERDUE + TAG_OPEN + getDate() + TAG_CLOSE
                + SPACE + _description;
        _description = _description.trim();
        setFloating(true);
    }

    @Override
    public Task merge(Task toMerge) {
        if (toMerge.getTitle() != null && !toMerge.getTitle().isEmpty()) {
            this.setTitle(toMerge.getTitle());
        }
        if (toMerge.getVenue() != null && !toMerge.getVenue().equals("null")) {
            this.setVenue(toMerge.getVenue());
        }
        if (toMerge.getDescription() != null
                && !toMerge.getDescription().isEmpty()) {
            this.setDescription(toMerge.getDescription());
        }
        if (toMerge.getDate() != null && !toMerge.getDate().equals("null")) {
            this.setDate(toMerge.getDate());
            removeOverdueComment();
        }
        if (toMerge.getTime() != null && !toMerge.getTime().equals("null")) {
            this.setTime(toMerge.getTime());
            removeOverdueComment();
        }
        if (toMerge.getEndTime() != null
                && !toMerge.getEndTime().equals("null")) {
            this.setEndTime(toMerge.getEndTime());
        } else {
            if (toMerge.getTime() != null && !toMerge.getTime().equals("null")) {
                this.setEndTime(null);
            }
        }
        return this;
    }

    private void removeOverdueComment() {
        if (this.isOverdue()) {
            int index = this.getDescription().indexOf("]") + 1;
            this.setDescription(_description.substring(index));
        }
    }
}
