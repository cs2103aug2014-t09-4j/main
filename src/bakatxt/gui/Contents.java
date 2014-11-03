// @author A0116538A
//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import bakatxt.core.Task;
import bakatxt.gui.theme.ThemeReader;
import bakatxt.international.BakaTongue;

/**
 * This class does the following:
 *
 * 1. Draw the alert message (i.e, visual feedback for user's input)
 * 2. Draw the date and content for each date
 *
 */

// TODO comments
class Contents extends JPanel {

    private static final String MESSAGE_EMPTY = "null";

    private static final int DATE_AND_TASKS_START_POSITION = 1;

    public Contents(LinkedList<Task> tasks) {

        setOpaque(false);
        setBackground(UIHelper.TRANSPARENT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        LinkedList<Task> tempTasks = tasks;
        updateContents(tempTasks);
    }

    /**
     * Check if tasks is empty, displaying the appropriate events.
     *
     * @param tasks is all the tasks in the LinkedList<Task> we need to add
     */
    protected void updateContents(LinkedList<Task> tasks) {

        //try {
            addTasksByDate(tasks);
        //} catch (NullPointerException e) {
        //    setNoEvents();
        //}
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

        while (tasks.peek() != null) {

            currentDate = tasks.peek().getDate();

            setDateAndDay(setDayAndDateText(currentDate));
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

        while (isSameDate(tasks, currentDate)) {
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

        String taskDate = tasks.peek().getDate();

        if (currentDate == null) {
            currentDate = MESSAGE_EMPTY;
        }
        if (taskDate == null) {
            taskDate = MESSAGE_EMPTY;
        }
        return taskDate.equals(currentDate);
    }

    private int addCurrentEvents(LinkedList<Task> tasks, int y, int offset) {

        assert (y > 0) : "y must be greater than zero";
        assert (offset > 0) : "offset must be at least 1";
        int taskNumber = y - offset;
        TaskBox taskBox;

        if (tasks.size() == 1) {
            taskBox = new OnlyTaskBox(tasks.pop(), taskNumber,
                          alternatingColors(taskNumber));

            setEvents(taskBox);
            shouldAnimate(taskBox);
            taskNumber++;

        } else {
            taskBox = new FirstTaskBox(tasks.pop(), taskNumber,
                          alternatingColors(taskNumber));

            setEvents(taskBox);
            taskNumber++;
            while (true) {

                if (tasks.size() == 1) {
                    taskBox = new FinalTaskBox(tasks.pop(), taskNumber,
                                  alternatingColors(taskNumber));

                    setEvents(taskBox);
                    shouldAnimate(taskBox);
                    taskNumber++;
                    break;
                }
                taskBox = new MiddleTaskBox(tasks.pop(), taskNumber,
                              alternatingColors(taskNumber));

                setEvents(taskBox);
                taskNumber++;
            }
        }

        return taskNumber + offset;
    }

    private static Color alternatingColors(int taskNumber) {
        if (taskNumber % 2 == 0) {
            return ThemeReader.getTaskDarkColor();
        }
        return ThemeReader.getTaskLightColor();
    }

    /**
     * @param alertMessage is the message to put in the layout specified
     */
    private void setDateAndDay(FormattedText dateAndDay) {
        this.add(dateAndDay);
    }

    private static void shouldAnimate(TaskBox task) {
        /*
        final UIAnimator animate = new UIAnimator(task);
        final boolean isNewTask = BakaUI.isNewTask();
        if (BakaUI.isNewTask()) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (BakaUI.isNewTask()) {
                    animate.flashComponent();
                }
            }
        });
        }
        //Thread t = new Thread(r);
        //t.start();
         */
    }

    /**
     * @param task is the message to put in the layout specified
     */
    private void setEvents(TaskBox task) {
        setTaskBoxSize(task);
        this.add(task);
    }

    // TODO probably a better method to do this
    private void setNoEvents() {
        FormattedText task = new FormattedText("You have no events!",
                                               ThemeReader.getTitleTheme());
        this.add(task);
    }

    private static void setTaskBoxSize(TaskBox task) {
        task.setMinimumSize(new Dimension(634, 100));
        task.setPreferredSize(new Dimension(634, 100));
        task.setMaximumSize(new Dimension(634, 100));
    }

    /**
     * @param dayAndDate is the string to style
     * @return a day and date FormattedText with the string
     */
    private static FormattedText setDayAndDateText(String dayAndDate) {

        if (dayAndDate == null || dayAndDate.equals(MESSAGE_EMPTY)) {
            dayAndDate = BakaTongue.getString("MESSAGE_FLOATING");
        }

        return new FormattedText(dayAndDate, ThemeReader.getDateTheme());
    }
}
