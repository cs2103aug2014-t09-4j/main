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
            initializeFromDatabaseString(tokenizedInput);
            updateFloatingStatus();
        }
    }

    /**
     * Checks that the task from the storage file contains all the relevant
     * information.
     * 
     * @param input
     *            a <code>String</code> from the storage file
     * @return <code>true</code> if the string is a valid task,
     *         <code>false</code> otherwise.
     */
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

    /**
     * Extracts the information of a <code>Task</code> into its proper
     * attributes of a <code>Task</code> instance.
     * 
     * @param tokenizedInput
     *            a <code>List</code> containing all the <code>String</code>
     *            tokens of the <code>String</code> from the storage file.
     */
    private void initializeFromDatabaseString(List<String> tokenizedInput) {
        int titleIndex = tokenizedInput.indexOf(TAG_TITLE) + 1;
        StringBuilder title = new StringBuilder();

        // ensure capturing of titles with more than one word
        for (int i = titleIndex; i < tokenizedInput.indexOf(TAG_DATE); i++) {
            title.append(tokenizedInput.get(i));
            title.append(SPACE);
        }
        _title = title.toString().trim();

        // retrieving the indices of the information of the Task
        int dateIndex = tokenizedInput.indexOf(TAG_DATE) + 1;
        int timeIndex = tokenizedInput.indexOf(TAG_TIME) + 1;
        int endTimeIndex = tokenizedInput.indexOf(TAG_ENDTIME) + 1;
        int venueIndex = tokenizedInput.indexOf(TAG_VENUE) + 1;
        int doneIndex = tokenizedInput.indexOf(TAG_DONE) + 1;
        int deletedIndex = tokenizedInput.indexOf(TAG_DELETED) + 1;
        int descriptionIndex = tokenizedInput.indexOf(TAG_DESCRIPTION) + 1;

        // date and time
        _date = tokenizedInput.get(dateIndex);
        _time = tokenizedInput.get(timeIndex);
        _endTime = tokenizedInput.get(endTimeIndex);

        // ensure capture of multiple words of the venue
        for (int i = venueIndex; i < doneIndex - 1; i++) {
            if (_venue == null) {
                _venue = tokenizedInput.get(i);
            } else {
                _venue = _venue + SPACE + tokenizedInput.get(i);
            }
        }

        // boolean flags of done, floating and deleted
        _isDone = tokenizedInput.get(doneIndex).equals(TAG_TRUE);
        _isFloating = _date.equals(_time);
        _isDeleted = tokenizedInput.get(deletedIndex).equals(TAG_TRUE);

        // capture the remaining information as the description
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

        // if task is not done, update accordingly of its overdue status
        if (_isDone == false) {
            updateOverdueStatus();
        }
    }

    /**
     * @return title of the task
     */
    @Override
    public String getTitle() {
        return _title;
    }

    /**
     * @return date of the task
     */
    @Override
    public String getDate() {
        return _date;
    }

    /**
     * Format valid dates into a more user-friendly format for GUI. Retains
     * special date <code>String</code> to ensure duality of the date field in
     * GUI.
     * 
     * @return a formatted date of the task if valid
     */
    @Override
    public String getFormattedDate() {
        BakaParser parser = new BakaParser();
        if (_date == null || _date.equals("null")) {
            return null;
        }
        String formatted = parser.getFormattedDate(_date);
        if (formatted == null || formatted.equals("null")) {
            return _date;
        }
        return formatted;
    }

    /**
     * @return start time of the task
     */
    @Override
    public String getTime() {
        return _time;
    }

    /**
     * @return end time of the task
     */
    @Override
    public String getEndTime() {
        return _endTime;
    }

    /**
     * @return venue of the task
     */
    @Override
    public String getVenue() {
        return _venue;
    }

    /**
     * @return description of the task
     */
    @Override
    public String getDescription() {
        return _description;
    }

    /**
     * Sets the title of the task
     * 
     * @param input
     *            containing the title of the task
     * 
     * @return <code>String</code> containing the updated title of the task
     */
    @Override
    public String setTitle(String input) {
        _title = input.trim();
        return _title;
    }

    /**
     * Sets the date of the task, and update the floating status of the task
     * accordingly.
     * 
     * @param input
     *            containing the date of the task
     * 
     * @return <code>String</code> containing the updated date of the task
     */
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

    /**
     * Sets the floating status to <code>true</code> if there is no date and
     * time specified, else set to <code>false</code>.
     */
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

    /**
     * Sets the time of the task, and update the floating status of the task
     * accordingly.
     * 
     * @param input
     *            containing the time of the task
     * 
     * @return <code>String</code> containing the updated time of the task
     */
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

    /**
     * Sets the end time of the task, and update the floating status of the task
     * accordingly. If the start time is <code>null</code>, the end time will be
     * set as start time.
     * 
     * @param input
     *            containing the end time of the task
     * 
     * @return <code>String</code> containing the updated end time of the task
     */
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

    /**
     * @param input
     *            containing the venue of the task
     * 
     * @return <code>String</code> containing the updated venue of the task
     */
    @Override
    public String setVenue(String input) {
        if (input == null || input.equals("null")) {
            _venue = null;
        } else {
            _venue = input.trim();
        }
        return _venue;
    }

    /**
     * @param input
     *            containing the venue of the task
     * 
     * @return <code>String</code> containing the updated venue of the task
     */
    @Override
    public String setDescription(String input) {
        if (input == null || input.equals("null")) {
            _description = null;
        } else {
            _description = input.trim();
        }
        return _description;
    }

    /**
     * @return <code>true</code> if the task is done, <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean isDone() {
        return _isDone;
    }

    /**
     * @return a formatted <code>String</code> of the task
     */
    @Override
    public String toDisplayString() {
        StringBuilder task = new StringBuilder();

        task.append(TAG_TITLE + SPACE + _title + SPACE);
        if (_date != null && !_date.equals(TAG_NULL)) {
            task.append(TAG_DATE + SPACE + _date + SPACE);
        }
        if (_time != null && !_time.equals(TAG_NULL)) {
            task.append(TAG_TIME + SPACE + _time + SPACE);
        }
        if (_endTime != null && !_endTime.equals(TAG_NULL)) {
            task.append(TAG_ENDTIME + SPACE + _endTime + SPACE);
        }
        if (_venue != null && !_venue.equals(TAG_NULL)) {
            task.append(TAG_VENUE + SPACE + _venue + SPACE);
        }
        if (_description != null && !_description.equals(TAG_NULL)
                && _description.isEmpty()) {
            task.append(TAG_DESCRIPTION + SPACE + _description + SPACE);
        }
        
        return task.toString();
    }

    /**
     * @return a <code>String</code> containing the relevant information of the
     *         task that is required to maintain integrity of the task and
     *         enable correct sorting of the tasks in the storage file.
     */
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

    /**
     * Sets a task to be deleted from the storage file.
     * 
     * @param <code>true</code> for task to be deleted, <code>false</code>
     *        otherwise.
     */
    @Override
    public void setDeleted(boolean isDeleted) {
        _isDeleted = isDeleted;
    }

    /**
     * @return <code>true</code> if task is floating, <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean isFloating() {
        return _isFloating;
    }

    /**
     * Sets a task to be done or undone. If task is done, the date of an overdue
     * task is restored. If task is not done, overdue status is updated
     * accordingly.
     * 
     * @param <code>true</code> for task to be done, <code>false</code> for task
     *        to be undone.
     */
    @Override
    public void setDone(boolean isDone) {
        _isDone = isDone;
        if (_isDone && isOverdue()) {
            _date = _description.substring(_description.indexOf(TAG_OPEN) + 1,
                    _description.indexOf(TAG_CLOSE));
            removeOverdueComment();
        } else if (!_isDone) {
            updateOverdueStatus();
        }
        updateFloatingStatus();
    }

    /**
     * Sets a task floating status.
     * 
     * @param <code>true</code> for task to be floating, <code>false</code>
     *        otherwise.
     */
    @Override
    public void setFloating(boolean isFloating) {
        _isFloating = isFloating;
        if (_isFloating) {
            _date = _time = _endTime = TAG_NULL;
        }
    }

    /**
     * @return <code>true</code> if the task is set to be deleted,
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isDeleted() {
        return _isDeleted;
    }

    /**
     * Determines if two tasks are equal through comparison of the
     * <code>String</code> to be written to the storage file.
     * 
     * @param Task
     *            to be compared with
     * 
     * @return <code>true</code> if both tasks contain the same information,
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task)) {
            return false;
        }
        Task task = (Task) obj;
        return this.toString().equals(task.toString());
    }

    /**
     * Sets the comparison of two tasks to be time-based to ensure that the
     * tasks are sorted chronologically in the storage file and
     * <code>Database</code>
     * 
     * @param Task
     *            to be compared with
     * 
     * @return <code>0</code> if they are of the same date and time,
     *         <code>1</code> or <code>-1</code> if they are of different time.
     */
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

    /**
     * Generates the key of which the task is placed in the memory.
     * 
     * @return <code>String</code> key of the task.
     */
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

    /**
     * Checks if a task is overdue based on the overdue status in the
     * description.
     * 
     * @return <code>true</code> if the task is overdue, <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean isOverdue() {
        if (_description==null || !_description.contains(STRING_OVERDUE)) {
            return false;
        }
        return true;
    }

    /**
     * Updates the overdue status of a task against the current time. Sets task
     * to be floating and prepend overdue information to the description if task
     * is overdue.
     */
    @Override
    public void updateOverdueStatus() {
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

    /**
     * Prepends the overdue information to the description and updates the date
     * and time accordingly.
     */
    private void setOverdue() {
        if (_description == null) {
            _description = new String();
        }
        _description = STRING_OVERDUE + TAG_OPEN + getDate() + TAG_CLOSE
                + SPACE + _description;
        _description = _description.trim();
        setFloating(true);
    }

    /**
     * Merges two tasks into one, with <code>null</code> fields being of the
     * lowest priorities. Information from task specified has a higher priority
     * over existing information in the <code>Task</code> instance.
     * 
     * @param Task
     *            containing information to be merged
     * 
     * @return Task containing information of updated set of information.
     */
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

    /**
     * Removes the overdue information from the description of the task
     */
    private void removeOverdueComment() {
        if (this.isOverdue()) {
            int index = this.getDescription().indexOf("]") + 1;
            this.setDescription(_description.substring(index));
        }
    }
}
