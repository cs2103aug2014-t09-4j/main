//@author A0116538A

package bakatxt.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JPanel;

import bakatxt.core.Task;
import bakatxt.gui.look.BakaAnimator;
import bakatxt.gui.look.ThemeReader;
import bakatxt.gui.look.UIHelper;

/**
 * This class places the elements of a single task correctly in their box.
 *
 */
abstract class TaskBox extends JPanel implements BakaAnimator {

    protected static final boolean IS_LINE_WRAP = true;
    private static final String AT = " @";

    private static final String DONE = "✓";
    private static final String UNDONE = "✗";
    private static final String OVERDUE = UNDONE + "!";

    private final Color _baseColor;

    public TaskBox(Task task, int index, Color backgroundColor) {

        setOpaque(false);
        setBackground(backgroundColor);
        setLayout(new GridBagLayout());
        addComponentsToPane(task, index);

        _baseColor = backgroundColor;
    }

    /**
     * Draws the various components in a taskbox
     *
     * @param task
     *        is the task to draw on the taskbox
     * @param index is the position within bakatxt where the taskbox
     */
    protected void addComponentsToPane(Task task, int index) {
        GridBagConstraints layout = new GridBagConstraints();

        setNumber(layout, index);
        setTaskAndLocation(layout, task.getTitle(), task.getVenue());
        setTimeStart(layout, task.getTime());
        setDescription(layout, task.getDescription());
        setTaskCompletionState(layout, doneState(task));
        setTimeEnd(layout, task.getEndTime());
    }

    /**
     * draw the index number of the task
     *
     * @param layout
     *        the layout we are drawing the string on
     * @param number
     *        is the number we are writing
     */
    private void setNumber(GridBagConstraints layout, int number) {

        FormattedText index = new FormattedText(shouldAddLeadingZero(number),
                                                ThemeReader.getNumberTheme());
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LAST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 0.0;
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 2 * UIHelper.BORDER, 0, 0);
        this.add(index, layout);
    }

    // TODO make locationText a different color
    /**
     * draw the task name and the location name of the task
     *
     * @param layout
     *        the layout we are drawing the string on
     * @param taskText
     *        the task name we are writing
     * @param locationText
     *        the location name we are writing
     */
    private void setTaskAndLocation(GridBagConstraints layout, String taskText,
                                    String locationText) {
        locationText = removeNullValues(AT, locationText);
        FormattedText task = new FormattedText(taskText + locationText,
                                               ThemeReader.getTitleTheme(),
                                               IS_LINE_WRAP);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.anchor = GridBagConstraints.LAST_LINE_START;
        layout.weightx = 0.01;
        layout.weighty = 0.0;
        layout.gridx = 1;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 5 * UIHelper.BORDER, 0, 0);
        this.add(task, layout);
    }

    /**
     * draw the description of the task
     *
     * @param layout
     *        the layout we are drawing the string on
     * @param descriptionText
     *        the description of the task we are writing
     */
    private void setDescription(GridBagConstraints layout, String descriptionText) {
        FormattedText description = new FormattedText(descriptionText,
                                                      ThemeReader.getDefaultTheme(),
                                                      IS_LINE_WRAP);
        layout.fill = GridBagConstraints.BOTH;
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridx = 1;
        layout.gridy = 1;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(2 * UIHelper.BORDER, 5 * UIHelper.BORDER, 0, 0);
        this.add(description, layout);
    }

    /**
     * draw the starting time of the task
     *
     * @param layout
     *        the layout we are drawing the string on
     * @param startTimeText
     *        the start time of the task we are writing
     */
    private void setTimeStart(GridBagConstraints layout, String startTimeText) {

        startTimeText = removeNullValues(startTimeText);
        FormattedText time = new FormattedText(startTimeText,
                                               ThemeReader.getDateTheme());
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.FIRST_LINE_END;
        layout.weightx = 0.01;
        layout.weighty = 0.1;
        layout.gridx = 2;
        layout.gridy = 0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(3 * UIHelper.BORDER, 0, 0,
                                   2 * UIHelper.BORDER);
        this.add(time, layout);
    }

    /**
     * draw whether or not the task has been completed
     *
     * @param layout
     *        the layout we are drawing the string on
     * @param state
     *        is the task completion state
     */
    private void setTaskCompletionState(GridBagConstraints layout, String state) {
        FormattedText completionState = new FormattedText(state,
                                                          ThemeReader.getLocationTheme());
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.PAGE_START;
        layout.weightx = 0.01;
        layout.weighty = 1.0;
        layout.gridx = 0;
        layout.gridy = 1;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(2 * UIHelper.BORDER, 2 * UIHelper.BORDER,
                                   0, 0);
        this.add(completionState, layout);
    }

    /**
     * draw the end time of the task
     *
     * @param layout
     *        the layout we are drawing the string on
     * @param endTimeText
     *        is the end time we are writing
     */
    private void setTimeEnd(GridBagConstraints layout, String endTimeText) {

        endTimeText = removeNullValues(endTimeText);
        FormattedText time = new FormattedText(endTimeText,
                                               ThemeReader.getDateTheme());
        layout.fill = GridBagConstraints.NONE;
        layout.anchor = GridBagConstraints.LAST_LINE_END;
        layout.weightx = 0.01;
        layout.weighty = 1.0;
        layout.gridx = 2;
        layout.gridy = 1;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets = new Insets(0, 0, 3 * UIHelper.BORDER,
                                   2 * UIHelper.BORDER);
        this.add(time, layout);
    }

    /**
     * if present, remove null || "null"  and return an empty string. else return
     * the old string.
     *
     * @param s
     *        the string to check
     * @return the string after processing
     */
    private static String removeNullValues(String s) {
        if (isStringNull(s)) {
            return "";
        }
        return s;
    }

    /**
     * if present, remove null || "null"  and return an empty string. else return
     * the old string with a prefix.
     *
     * @param prefix
     *        the prefix to append
     * @param s
     *        the string to check
     * @return the string after processing
     */
    private static String removeNullValues(String prefix, String s) {
        if (isStringNull(s)) {
            return "";
        }
        return prefix + s;
    }

    /**
     * Add a leading zero for numbers less than 10
     *
     * @param number
     *        the number to add the leading zero
     * @return the number with the leading zero as a string
     */
    private static String shouldAddLeadingZero(int number) {
        String num = Integer.toString(number);
        if (number < 10) {
            num = "0" + num;
        }
        return num;
    }

    /**
     * check if a string is null or "null"
     *
     * @param s
     *        the string to check
     * @return true if the string is either null value
     */
    private static boolean isStringNull(String s) {
        return (s == null || s.equals("null"));
    }

    /**
     * check the done state of the task and returns the appropriate string to
     * show the user
     *
     * @param task
     *        is the task to check the done state
     * @return a nice looking string to reflect that
     */
    private static String doneState(Task task) {
        if (task.isDone()) {
            return DONE;
        } else if (task.isOverdue()) {
            return OVERDUE;
        }
        return UNDONE;
    }

    /**
     * border is transparent
     */
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIHelper.TRANSPARENT);
    }

    // TODO does not work yet
    /**
     * @see {@link bakatxt.gui.look.BakaAnimator}
     */
    @Override
    public Color getColor() {
        return _baseColor;
    }

    /**
     * @see {@link bakatxt.gui.look.BakaAnimator}
     */
    @Override
    public void setColor(Color newColor) {
        setBackground(newColor);
    }

    /**
     * We are not animating the location for TaskBox
     */
    @Override
    public Point getPoint() {
        return null;
    }

    @Override
    public void setPoint(Point newLocation) {
    }
}
