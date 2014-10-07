//@author A0116538A

package bakatxt.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;

import javax.swing.JPanel;

import bakatxt.core.Task;

// TODO comments

class Contents extends JPanel {

    private static GridBagConstraints _layout = new GridBagConstraints();

    // TODO should not need to parse this
    private static final String MESSAGE_NO_DATE = "No date is specified for this task.";
    private static final String WELCOME_MESSAGE = "Welcome to BakaTXT! For help please type help in the box above";

    private static final int DATE_AND_TASKS_START_POSITION = 1;

    public Contents(LinkedList<Task> tasks) {

        setOpaque(false);
        setBackground(UIHelper.TRANSPARENT);
        setLayout(new GridBagLayout());
        updateContents(tasks);
    }

    protected void updateContents(LinkedList<Task> tasks) {
        // TODO set alert message to the specific alert
        setAlertMessage(setAlertMessageText(WELCOME_MESSAGE));

        if (tasks.peek() != null) {
            addTasksByDate(tasks);
        } else {
            setDateAndDay(setDayAndDateText(""), DATE_AND_TASKS_START_POSITION);
            setEvents(new TaskItems(null), DATE_AND_TASKS_START_POSITION + 1);
        }
    }

    private void addTasksByDate(LinkedList<Task> tasks) {
        String currentDate;
        TaskItems taskItems;
        int i = DATE_AND_TASKS_START_POSITION;

        while(tasks.peek() != null) {

            currentDate = tasks.peek().getDate();
            taskItems = new TaskItems(getAllTasksInOneDate(tasks));

            setDateAndDay(setDayAndDateText(currentDate), i);
            setEvents(taskItems, i + 1);

            i += 2;
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

    private static boolean isSameDate(LinkedList<Task> tasks, String currentDate) {
        return tasks.peek().getDate().equals(currentDate);
    }

    private void setAlertMessage(FormattedText alertMessage) {
        _layout.fill = GridBagConstraints.NONE;
        _layout.anchor = GridBagConstraints.PAGE_START;
        _layout.weightx = 0.0;
        _layout.weighty = 0.01;
        _layout.gridx = 0;
        _layout.gridy = 0;
        _layout.gridheight = 1;
        _layout.gridwidth = 1;
        this.add(alertMessage, _layout);
    }

    private void setDateAndDay(FormattedText dateAndDay, int y) {
        _layout.fill = GridBagConstraints.NONE;
        _layout.anchor = GridBagConstraints.LAST_LINE_END;
        _layout.weightx = 0.0;
        _layout.weighty = 0.01;
        _layout.gridx = 0;
        _layout.gridy = y;
        _layout.gridheight = 1;
        _layout.gridwidth = 1;
        this.add(dateAndDay, _layout);
    }

    private void setEvents(TaskItems tasks, int y) {
        _layout.fill = GridBagConstraints.BOTH;
        _layout.anchor = GridBagConstraints.FIRST_LINE_START;
        _layout.weightx = 1.0;
        _layout.weighty = 1.0;
        _layout.gridx = 0;
        _layout.gridy = y;
        _layout.gridheight = 1;
        _layout.gridwidth = GridBagConstraints.REMAINDER;
        _layout.insets = new Insets(UIHelper.WINDOW_BORDER, 0, 0, 0);
        this.add(tasks, _layout);
    }

    private static FormattedText setAlertMessageText (String message) {
        return new FormattedText(message, UIHelper.PRESET_TYPE_DEFAULT,
                UIHelper.PRESET_SIZE_DEFAULT, UIHelper.PRESET_COLOR_ALERT);
    }

    private static FormattedText setDayAndDateText (String dayAndDate) {

        if (dayAndDate.equals(MESSAGE_NO_DATE)) {
            dayAndDate = "Floating";
        }

        return new FormattedText(dayAndDate, UIHelper.PRESET_TYPE_DATE,
                UIHelper.PRESET_SIZE_DATE, UIHelper.PRESET_COLOR_DATE);
    }
}
