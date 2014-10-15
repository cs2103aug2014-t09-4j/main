//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import bakatxt.core.Task;

/**
 * This class does the following:
 *
 * 1. Draw the alert message (i.e, visual feedback for user's input)
 * 2. Draw the date and content for each date
 *
 */

// TODO merge in TaskItems class
// TODO comments
class Contents extends JPanel {

    private static GridBagConstraints _layout = new GridBagConstraints();
    private static boolean _isEven = true;

    // TODO should not need to print this, rather, take the thing to be printed from logic
    private static final String MESSAGE_WELCOME = "Welcome to BakaTXT! For help "
                                                + "please type help in the box above";
    private static final String MESSAGE_EMPTY = "null";

    private static final int DATE_AND_TASKS_START_POSITION = 1;

    public Contents(LinkedList<Task> tasks) {

        setOpaque(false);
        setBackground(UIHelper.TRANSPARENT);
        //setLayout(new GridBagLayout());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        LinkedList<Task> tempTasks = tasks;
        updateContents(tempTasks);
    }

    /**
     * Draw the alert message and check if tasks is empty, displaying the appropriate
     * events.
     *
     * @param tasks is all the tasks in the LinkedList<Task> we need to add
     */
    protected void updateContents(LinkedList<Task> tasks) {
        // TODO set alert message to the specific alert
        setAlertMessage(setAlertMessageText(MESSAGE_WELCOME));

        try {
            addTasksByDate(tasks);
        } catch (NullPointerException e) {
            setNoEvents();
            /*
            setDateAndDay(setDayAndDateText(""), DATE_AND_TASKS_START_POSITION);
            setEvents(new TaskItems(null), DATE_AND_TASKS_START_POSITION + 1);
            */
        }
    }

    /**
     * Takes a LinkedList<Task> and puts it into the dateAndDay and Events "boxes"
     *
     * @param tasks is all the tasks in the LinkedList<Task> we need to add
     */
    private void addTasksByDate(LinkedList<Task> tasks) {
        String currentDate;
        int y = DATE_AND_TASKS_START_POSITION;
        int offset = 0;

        while(tasks.peek() != null) {

            currentDate = tasks.peek().getDate();

            setDateAndDay(setDayAndDateText(currentDate), y);
            offset++;
            y = addCurrentEvents(getAllTasksInOneDate(tasks), y + 1, offset);
        }
    }

    /**
     * This method takes a LinkedList<Task> and splits that LinkedList by date into
     * smaller LinkedLists
     *
     * @param tasks is the task to be split
     * @return a LinkedList<Task> of the earliest date
     */
    private static LinkedList<Task> getAllTasksInOneDate(LinkedList<Task> tasks) {

        LinkedList<Task> seperateTasksByDate = new LinkedList<Task>();
        String currentDate = tasks.peek().getDate();

        while(isSameDate(tasks, currentDate)) {
            seperateTasksByDate.add(tasks.poll());
        }

        return seperateTasksByDate;
    }

    /**
     * Checks if the first element of a LinkedList<Task> has the same date as currentDate
     *
     * @param tasks is the LinkedList we are checking
     * @param currentDate is the date we are comparing to
     * @return true if the date is the same, false if it isn't or if the LinkedList
     *         is empty.
     */
    private static boolean isSameDate(LinkedList<Task> tasks, String currentDate) {
        if (tasks.peek() == null) {
            return false;
        }
        return tasks.peek().getDate().equals(currentDate);
    }

    private int addCurrentEvents(LinkedList<Task> tasks, int y, int offset) {

        assert(y > 0) : "y must be greater than zero";
        assert(offset > 0) : "offset must be at least 1";

        if (tasks.size() == 1) {
            setEvents(new OnlyTaskBox(tasks.pop(), y - offset, alternatingColors()), y);
            y++;

        } else {
            setEvents(new FirstTaskBox(tasks.pop(), y - offset, alternatingColors()), y);
            y++;
            while(true) {

                if (tasks.size() == 1) {
                    setEvents(new FinalTaskBox(tasks.pop(), y - offset, alternatingColors()), y);
                    y++;
                    break;
                }
                setEvents(new MiddleTaskBox(tasks.pop(), y - offset, alternatingColors()), y);
                y++;
            }
        }

    return y;
    }

    private static Color alternatingColors() {
        _isEven = !_isEven;
        if (_isEven) {
            return UIHelper.GRAY_BLACK;
        }
        return UIHelper.GRAY_DARK;
    }

    //TODO, move to BakaPanel
    /**
     * @param alertMessage is the message to put in the layout specified
     */
    private void setAlertMessage(FormattedText alertMessage) {
/*
        _layout.fill = GridBagConstraints.NONE;
        _layout.anchor = GridBagConstraints.PAGE_START;
        _layout.weightx = 0.0;
        _layout.weighty = 0.01;
        _layout.gridx = 0;
        _layout.gridy = 0;
        _layout.gridheight = 1;
        _layout.gridwidth = 1;
*/
        this.add(alertMessage);
    }

    /**
     * @param alertMessage is the message to put in the layout specified
     * @param y is the vertical order whereby it is placed
     */
    private void setDateAndDay(FormattedText dateAndDay, int y) {
/*
        _layout.fill = GridBagConstraints.NONE;
        _layout.anchor = GridBagConstraints.LAST_LINE_END;
        _layout.weightx = 0.0;
        _layout.weighty = 0.01;
        _layout.gridx = 0;
        _layout.gridy = y;
        _layout.gridheight = 1;
        _layout.gridwidth = 1;
        _layout.insets = new Insets(UIHelper.BORDER, 0, UIHelper.BORDER, 0);
*/
        setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.add(dateAndDay);
    }

    /**
     * @param task is the message to put in the layout specified
     * @param y is the vertical order whereby it is placed
     */
    private void setEvents(TaskBox task, int y) {
/*
        _layout.fill = GridBagConstraints.BOTH;
        _layout.anchor = GridBagConstraints.FIRST_LINE_START;
        _layout.weightx = 1.0;
        _layout.weighty = 1.0;
        _layout.gridx = 0;
        _layout.gridy = y;
        _layout.gridheight = 1;
        _layout.gridwidth = GridBagConstraints.REMAINDER;
        _layout.insets = new Insets(0, 0, 0, 0);
*/
        setAlignmentX(Component.CENTER_ALIGNMENT);
        task.setMinimumSize(new Dimension(634, 100));
        task.setPreferredSize(new Dimension(634, 100));
        task.setMaximumSize(new Dimension(634, 100));
        this.add(task);
    }

    //TODO probably a better method to do this

    private void setNoEvents() {

        FormattedText task = new FormattedText("You have no events!", UIHelper.PRESET_TYPE_TITLE,
                UIHelper.PRESET_SIZE_TITLE, UIHelper.PRESET_COLOR_TITLE);
        /*
        _layout.fill = GridBagConstraints.NONE;
        _layout.anchor = GridBagConstraints.CENTER;
        _layout.weightx = 1.0;
        _layout.weighty = 1.0;
        _layout.gridx = 0;
        _layout.gridy = 0;
        */
        this.add(task);
    }

    /**
     * @param message is the string to style
     * @return a alert message FormattedText with the string
     */
    private static FormattedText setAlertMessageText (String message) {
        return new FormattedText(message, UIHelper.PRESET_TYPE_DEFAULT,
                UIHelper.PRESET_SIZE_DEFAULT, UIHelper.PRESET_COLOR_ALERT);
    }

    /**
     * @param dayAndDate is the string to style
     * @return a day and date FormattedText with the string
     */
    private static FormattedText setDayAndDateText (String dayAndDate) {

        if (dayAndDate.equals(MESSAGE_EMPTY)) {
            dayAndDate = "Floating";
        }

        return new FormattedText(dayAndDate, UIHelper.PRESET_TYPE_DATE,
                UIHelper.PRESET_SIZE_DATE, UIHelper.PRESET_COLOR_DATE);
    }
}
